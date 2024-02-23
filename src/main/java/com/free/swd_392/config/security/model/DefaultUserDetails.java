package com.free.swd_392.config.security.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

/**
 * Default user detail
 */
public class DefaultUserDetails implements UserDetails {
    /**
     * authorized party
     */
    @JsonProperty("azp")
    public String issuedFor;
    /**
     * Prefix role
     */
    @JsonIgnore
    protected String prefixRole = "ROLE_";
    /**
     * Role anonymous
     */
    @JsonIgnore
    protected String anonymous = "ROLE_ANONYMOUS";
    /**
     * Resource access
     */
    @JsonProperty("resource_access")
    protected transient Map<String, Access> resourceAccess;

    /**
     * Scope
     */
    @JsonProperty("scope")
    protected String scope;

    /**
     * Json web token id
     */
    @JsonProperty("jti")
    protected String id;
    /**
     * Expiration time
     */
    @JsonProperty("exp")
    protected Long exp;
    /**
     * Not Before
     */
    @JsonProperty("nbf")
    protected Long nbf;
    /**
     * Issued At
     */
    @JsonProperty("iat")
    protected Long iat;
    /**
     * Issuer
     */
    @JsonProperty("iss")
    protected String issuer;
    /**
     * subject
     */
    @JsonProperty("sub")
    protected String subject;
    /**
     * Type
     */
    @JsonProperty("typ")
    protected String type;
    /**
     * Value used to associate a Client session with an ID Token
     */
    @JsonProperty("nonce")
    protected String nonce;
    /**
     * Time when the authentication occurred
     */
    @JsonProperty("auth_time")
    protected Long authTime;
    /**
     * Session ID
     */
    @JsonProperty("session_state")
    @JsonAlias("sid")
    protected String sessionState;
    /**
     * Access Token hash value
     */
    @JsonProperty("at_hash")
    protected String accessTokenHash;
    /**
     * Code hash value
     */
    @JsonProperty("c_hash")
    protected String codeHash;
    /**
     * Full name
     */
    @JsonProperty("name")
    protected String name;
    /**
     * Given name(s) or first name(s)
     */
    @JsonProperty("given_name")
    protected String givenName;
    /**
     * Surname(s) or last name(s)
     */
    @JsonProperty("family_name")
    protected String familyName;
    /**
     * Middle name(s)
     */
    @JsonProperty("middle_name")
    protected String middleName;
    /**
     * Casual name
     */
    @JsonProperty("nickname")
    protected String nickName;
    /**
     * Shorthand name by which the End-User wishes to be referred to
     */
    @JsonProperty("preferred_username")
    protected String preferredUsername;
    /**
     * Profile page URL
     */
    @JsonProperty("profile")
    protected String profile;
    /**
     * Profile picture URL
     */
    @JsonProperty("picture")
    protected String picture;
    /**
     * Web page or blog URL
     */
    @JsonProperty("website")
    protected String website;
    /**
     * Preferred e-mail address
     */
    @JsonProperty("email")
    protected String email;
    /**
     * True if the e-mail address has been verified; otherwise false
     */
    @JsonProperty("email_verified")
    protected Boolean emailVerified;
    /**
     * Gender
     */
    @JsonProperty("gender")
    protected String gender;
    /**
     * Birthday
     */
    @JsonProperty("birthdate")
    protected String birthdate;
    /**
     * Time zone
     */
    @JsonProperty("zone_info")
    protected String zoneInfo;
    /**
     * Locale
     */
    @JsonProperty("locale")
    protected String locale;
    /**
     * Preferred telephone number
     */
    @JsonProperty("phone_number")
    protected String phoneNumber;
    /**
     * True if the phone number has been verified; otherwise false
     */
    @JsonProperty("phone_number_verified")
    protected Boolean phoneNumberVerified;
    /**
     * Preferred postal address
     */
    @JsonProperty("address")
    protected AddressClaimSet address;
    /**
     * Time the information was last updated
     */
    @JsonProperty("updated_at")
    protected Long updatedAt;
    /**
     * claims_locales
     */
    @JsonProperty("claims_locales")
    protected String claimsLocales;
    /**
     * Authentication Context Class Reference
     */
    @JsonProperty("acr")
    protected String acr;
    /**
     * State hash
     */
    @JsonProperty("s_hash")
    protected String stateHash;
    /**
     * Realm access
     */
    @JsonProperty("realm_access")
    protected Access realmAccess;
    /**
     * Claims
     */
    protected transient Map<String, Object> claims;

    public DefaultUserDetails() {
        // empty
    }

    /**
     * Get resource access
     *
     * @return {@link #resourceAccess}
     */
    public Map<String, Access> getResourceAccess() {
        return new HashMap<>(resourceAccess);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (Objects.nonNull(realmAccess)) {
            for (String role : realmAccess.getRoles()) {
                authorities.add(
                        DefaultGrantedAuthority
                                .builder()
                                .authority(getPrefixRole().concat(role.toUpperCase()))
                                .build()
                );
            }
        }

        if (Objects.nonNull(resourceAccess)) {
            for (Map.Entry<String, Access> resource : resourceAccess.entrySet()) {
                for (String role : resource.getValue().getRoles()) {
                    authorities.add(DefaultGrantedAuthority
                            .builder()
                            .authority(getPrefixRole().concat(role.toUpperCase()))
                            .build()
                    );
                }
            }
        }
        if (authorities.isEmpty()) {
            DefaultGrantedAuthority gameGrantedAuthority = DefaultGrantedAuthority
                    .builder()
                    .authority(getAnonymous())
                    .build();
            authorities = Collections.singletonList(gameGrantedAuthority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return preferredUsername;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonAnyGetter
    public Map<String, Object> getClaims() {
        return claims;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public String getIssuedFor() {
        return issuedFor;
    }

    public String getPrefixRole() {
        return prefixRole;
    }

    public String getScope() {
        return scope;
    }

    public String getId() {
        return id;
    }

    public Long getExp() {
        return exp;
    }

    public Long getNbf() {
        return nbf;
    }

    public Long getIat() {
        return iat;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getNonce() {
        return nonce;
    }

    public Long getAuthTime() {
        return authTime;
    }

    public String getSessionState() {
        return sessionState;
    }

    public String getAccessTokenHash() {
        return accessTokenHash;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public String getProfile() {
        return profile;
    }

    public String getPicture() {
        return picture;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getZoneInfo() {
        return zoneInfo;
    }

    public String getLocale() {
        return locale;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public AddressClaimSet getAddress() {
        return address;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getClaimsLocales() {
        return claimsLocales;
    }

    public String getAcr() {
        return acr;
    }

    public String getStateHash() {
        return stateHash;
    }

    public Access getRealmAccess() {
        return realmAccess;
    }

    @JsonProperty("azp")
    public void setIssuedFor(String issuedFor) {
        this.issuedFor = issuedFor;
    }

    @JsonIgnore
    public void setPrefixRole(String prefixRole) {
        this.prefixRole = prefixRole;
    }

    @JsonIgnore
    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    @JsonProperty("resource_access")
    public void setResourceAccess(Map<String, Access> resourceAccess) {
        this.resourceAccess = resourceAccess;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonProperty("jti")
    public void setId(String id) {
        this.id = id;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public void setNbf(Long nbf) {
        this.nbf = nbf;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    @JsonProperty("iss")
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @JsonProperty("sub")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("typ")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("nonce")
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @JsonProperty("auth_time")
    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    @JsonAlias("sid")
    @JsonProperty("session_state")
    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    @JsonProperty("at_hash")
    public void setAccessTokenHash(String accessTokenHash) {
        this.accessTokenHash = accessTokenHash;
    }

    @JsonProperty("c_hash")
    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("given_name")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @JsonProperty("family_name")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @JsonProperty("middle_name")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("nickname")
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @JsonProperty("preferred_username")
    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    @JsonProperty("profile")
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @JsonProperty("picture")
    public void setPicture(String picture) {
        this.picture = picture;
    }

    @JsonProperty("website")
    public void setWebsite(String website) {
        this.website = website;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("email_verified")
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("birthdate")
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @JsonProperty("zone_info")
    public void setZoneInfo(String zoneInfo) {
        this.zoneInfo = zoneInfo;
    }

    @JsonProperty("locale")
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @JsonProperty("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("phone_number_verified")
    public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    @JsonProperty("address")
    public void setAddress(AddressClaimSet address) {
        this.address = address;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("claims_locales")
    public void setClaimsLocales(String claimsLocales) {
        this.claimsLocales = claimsLocales;
    }

    @JsonProperty("acr")
    public void setAcr(String acr) {
        this.acr = acr;
    }

    @JsonProperty("s_hash")
    public void setStateHash(String stateHash) {
        this.stateHash = stateHash;
    }

    @JsonProperty("realm_access")
    public void setRealmAccess(Access realmAccess) {
        this.realmAccess = realmAccess;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    /**
     * Access
     */
    public static class Access implements Serializable {
        /**
         * Roles
         */
        @JsonProperty("roles")
        protected transient Set<String> roles;
        /**
         * Verify caller
         */
        @JsonProperty("verify_caller")
        protected Boolean verifyCaller;

        protected Access(Set<String> roles, Boolean verifyCaller) {
            this.roles = roles;
            this.verifyCaller = verifyCaller;
        }

        protected Access() {
        }

        public static AccessBuilder builder() {
            return new AccessBuilder();
        }

        /**
         * Get roles
         *
         * @return {@link #roles}
         */
        public Set<String> getRoles() {
            return roles;
        }

        /**
         * Set roles
         *
         * @param roles roles
         * @return {@link Access}
         */
        public Access roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        /**
         * @param role role
         * @return True if user in role, else false
         */
        @JsonIgnore
        public boolean isUserInRole(String role) {
            if (roles == null) {
                return false;
            }
            return roles.contains(role);
        }

        /**
         * Add role
         *
         * @param role role
         * @return {@link Access}
         */
        public Access addRole(String role) {
            if (roles == null) {
                roles = new HashSet<>();
            }
            roles.add(role);
            return this;
        }

        /**
         * getVerifyCaller
         *
         * @return {@link #verifyCaller}
         */
        public Boolean getVerifyCaller() {
            return verifyCaller;
        }

        /**
         * verifyCaller
         *
         * @param required required
         * @return {@link Access}
         */
        public Access verifyCaller(Boolean required) {
            verifyCaller = required;
            return this;
        }

        public static class AccessBuilder {
            private Set<String> roles;
            private Boolean verifyCaller;

            AccessBuilder() {
            }

            @JsonProperty("roles")
            public AccessBuilder roles(Set<String> roles) {
                this.roles = roles;
                return this;
            }

            @JsonProperty("verify_caller")
            public AccessBuilder verifyCaller(Boolean verifyCaller) {
                this.verifyCaller = verifyCaller;
                return this;
            }

            public Access build() {
                return new Access(roles, verifyCaller);
            }

            @Override
            public String toString() {
                return "DefaultUserDetails.Access.AccessBuilder(roles=" + roles + ", verifyCaller=" + verifyCaller + ")";
            }
        }
    }

    /**
     * AddressClaimSet
     */
    public static class AddressClaimSet implements Serializable {
        /**
         * Formatted address
         */
        @JsonProperty("formatted")
        protected String formattedAddress;
        /**
         * Street address
         */
        @JsonProperty("street_address")
        protected String streetAddress;
        /**
         * locality
         */
        @JsonProperty("locality")
        protected String locality;
        /**
         * region
         */
        @JsonProperty("region")
        protected String region;
        /**
         * postal code
         */
        @JsonProperty("postal_code")
        protected String postalCode;
        /**
         * country
         */
        @JsonProperty("country")
        protected String country;

        protected AddressClaimSet(String formattedAddress, String streetAddress, String locality, String region, String postalCode, String country) {
            this.formattedAddress = formattedAddress;
            this.streetAddress = streetAddress;
            this.locality = locality;
            this.region = region;
            this.postalCode = postalCode;
            this.country = country;
        }

        protected AddressClaimSet() {
        }

        public static AddressClaimSetBuilder builder() {
            return new AddressClaimSetBuilder();
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public String getLocality() {
            return locality;
        }

        public String getRegion() {
            return region;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getCountry() {
            return country;
        }

        public static class AddressClaimSetBuilder {
            private String formattedAddress;
            private String streetAddress;
            private String locality;
            private String region;
            private String postalCode;
            private String country;

            AddressClaimSetBuilder() {
            }

            @JsonProperty("formatted")
            public AddressClaimSetBuilder formattedAddress(String formattedAddress) {
                this.formattedAddress = formattedAddress;
                return this;
            }

            @JsonProperty("street_address")
            public AddressClaimSetBuilder streetAddress(String streetAddress) {
                this.streetAddress = streetAddress;
                return this;
            }

            @JsonProperty("locality")
            public AddressClaimSetBuilder locality(String locality) {
                this.locality = locality;
                return this;
            }

            @JsonProperty("region")
            public AddressClaimSetBuilder region(String region) {
                this.region = region;
                return this;
            }

            @JsonProperty("postal_code")
            public AddressClaimSetBuilder postalCode(String postalCode) {
                this.postalCode = postalCode;
                return this;
            }

            @JsonProperty("country")
            public AddressClaimSetBuilder country(String country) {
                this.country = country;
                return this;
            }

            public AddressClaimSet build() {
                return new AddressClaimSet(formattedAddress, streetAddress, locality, region, postalCode, country);
            }

            @Override
            public String toString() {
                return "DefaultUserDetails.AddressClaimSet.AddressClaimSetBuilder(formattedAddress=" + formattedAddress + ", streetAddress=" + streetAddress + ", locality=" + locality + ", region=" + region + ", postalCode=" + postalCode + ", country=" + country + ")";
            }
        }
    }
}
