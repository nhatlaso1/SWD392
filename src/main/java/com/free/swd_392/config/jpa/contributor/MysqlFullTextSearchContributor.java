package com.free.swd_392.config.jpa.contributor;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.query.ReturnableType;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.SqlAppender;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.SqlTypes;

import java.util.List;

import static com.free.swd_392.shared.constant.SqlFunctionName.FULL_TEXT_SEARCH;

public class MysqlFullTextSearchContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .register(FULL_TEXT_SEARCH, new MysqlFullTextSearchFunction(FULL_TEXT_SEARCH));
    }

    public static class MysqlFullTextSearchFunction extends StandardSQLFunction {

        private static final BasicTypeReference<Boolean> RETURN_TYPE = new BasicTypeReference<>("boolean", Boolean.class, SqlTypes.BOOLEAN);

        MysqlFullTextSearchFunction(final String functionName) {
            super(functionName, true, RETURN_TYPE);
        }

        @Override
        public void render(SqlAppender sqlAppender, List<? extends SqlAstNode> arguments, ReturnableType<?> returnType, SqlAstTranslator<?> translator) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Function " + getName() + " requires exactly 2 arguments");
            }
            sqlAppender.append("match(");
            arguments.get(0).accept(translator);
            sqlAppender.append(") against (");
            arguments.get(1).accept(translator);
            sqlAppender.append(")");
        }
    }
}
