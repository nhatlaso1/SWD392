package com.free.swd_392.entity.location;

import com.free.swd_392.enums.LocationKind;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@FieldNameConstants
@Table(name = TableName.LOCATION)
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence_generator")
    @SequenceGenerator(name = "location_sequence_generator")
    private Long id;
    @Column(columnDefinition = "VARCHAR(50) NOT NULL, FULLTEXT KEY nameFullText(name)")
    private String name;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private LocationKind kind;
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "parent_id",
            foreignKey = @ForeignKey(name = "fk_location_parent_id")
    )
    private LocationEntity parent;
    @ToString.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true,
            targetEntity = LocationEntity.class,
            mappedBy = "parent"
    )
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<LocationEntity> childLocation;

    public LocationEntity(String name, LocationKind kind) {
        this(name, kind, null);
    }

    public LocationEntity(String name, LocationKind kind, LocationEntity parent) {
        this.name = name;
        this.kind = kind;
        this.parent = parent;
    }

    public void addChild(LocationEntity child) {
        if (childLocation == null) {
            childLocation = new ArrayList<>();
        }
        childLocation.add(child);
        child.setParent(this);
    }
}
