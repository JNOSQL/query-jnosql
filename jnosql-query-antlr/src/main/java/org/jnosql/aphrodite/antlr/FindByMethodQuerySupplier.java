/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jnosql.query.SelectQuery;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

class FindByMethodQuerySupplier extends MethodBaseListener implements BiFunction<String, String, SelectQuery> {

    @Override
    public SelectQuery apply(String query, String entity) {
        Objects.requireNonNull(query, " query is required");
        Objects.requireNonNull(entity, " entity is required");
        runQuery(MethodQuery.of(query).get(), entity);
        return null;
    }

    @Override
    public void exitEq(MethodParser.EqContext ctx) {
        System.out.println("eq");
        super.exitEq(ctx);
    }

    @Override
    public void exitVariable(MethodParser.VariableContext ctx) {
        System.out.println("variable" + ctx.getText());
    }

    private void runQuery(String query, String entity) {

        CharStream stream = CharStreams.fromString(query);
        MethodLexer lexer = new MethodLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MethodParser parser = new MethodParser(tokens);
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(QueryErrorListener.INSTANCE);
        parser.addErrorListener(QueryErrorListener.INSTANCE);

        ParseTree tree = getParserTree().apply(parser);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, tree);
    }

    Function<MethodParser, ParseTree> getParserTree() {
        return MethodParser::findBy;
    }


}
