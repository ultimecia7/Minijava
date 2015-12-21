package gen;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ultimecia on 12/19/15.
 */
public class BuildAST extends MinijavaBaseVisitor<Klass> {


    @Override
    public Klass visitMainClass(@NotNull MinijavaParser.MainClassContext ctx) {
        ctx.setPath("/Main Class:"+ctx.Identifier(0).getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitClassDeclaration(@NotNull MinijavaParser.ClassDeclarationContext ctx) {
        ctx.setPath("/Class:" + ctx.Identifier(0).getText());
        System.out.println(ctx.getPath());
        if(ctx.Identifier().size() == 2)
        {
            System.out.println(ctx.getPath() + "/SuperClass" + ctx.Identifier(1).getText());
        }
        return visitChildren(ctx);
    }

    @Override public Klass visitFieldDeclaration(@NotNull MinijavaParser.FieldDeclarationContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Field");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitLocalDeclaration(@NotNull MinijavaParser.LocalDeclarationContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Local");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitMethodDeclaration(@NotNull MinijavaParser.MethodDeclarationContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/MethodDecl");
        System.out.println(ctx.getPath());
        System.out.println(ctx.getPath() + "/MethodName:" + ctx.Identifier().getText());
        ctx.setPath(ctx.getPath() + "/RetType");
        visit(ctx.type());
        if(ctx.parameterList() != null) {
            ctx.setPath(ctx.getPath() + "/Params");
            visit(ctx.parameterList());
        }
        ctx.setPath(ctx.getPath() + "/MethodBody");
        visit(ctx.methodBody());
        return null;
    }

    @Override public Klass visitParameter(@NotNull MinijavaParser.ParameterContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ParamDecl");
        System.out.println(ctx.getPath());
        System.out.println(ctx.getPath() + "/ParamName:" + ctx.Identifier());
        ctx.setPath(ctx.getParent().getPath() + "/ParamDecl/Type");
        return visitChildren(ctx);
    }

    @Override public Klass visitParameterList(@NotNull MinijavaParser.ParameterListContext ctx) {
        List<Klass> parameterList = new ArrayList<Klass>();
        int paramCount = 0;
        for(MinijavaParser.ParameterContext paramCtx : ctx.parameter().subList(1, ctx.parameter().size())){
            ctx.setPath(ctx.getParent().getPath() + "/Param" + paramCount);
            parameterList.add(visit(paramCtx));
            paramCount++;
        }
        return null;
    }

    @Override public Klass visitMethodBody(@NotNull MinijavaParser.MethodBodyContext ctx) {
        if(ctx.localDeclaration().size() > 0)
        {
            ctx.setPath(ctx.getParent().getPath() + "/Locals");
            System.out.println(ctx.getPath());
            int localCount = 0;
            for(MinijavaParser.LocalDeclarationContext localDecl : ctx.localDeclaration().subList(0, ctx.localDeclaration().size())){
                ctx.setPath(ctx.getParent().getPath() + "/Local" + localCount);
                visit(localDecl);
                localCount++;
            }
        }
        if(ctx.statement().size() > 0)
        {
            ctx.setPath(ctx.getParent().getPath() + "/Stmts");
            System.out.println(ctx.getPath());
            int stmtCount = 0;
            for(MinijavaParser.StatementContext stmt : ctx.statement().subList(0, ctx.statement().size())){
                ctx.setPath(ctx.getParent().getPath() + "/Stmt" + stmtCount);
                visit(stmt);
                stmtCount++;
            }
        }
        ctx.setPath(ctx.getParent().getPath() + "/RetExpr");
        visit(ctx.expression());

        return null;
    }

    @Override public Klass visitVarDeclaration(@NotNull MinijavaParser.VarDeclarationContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/VarDecl");
        System.out.println(ctx.getPath());
        System.out.println(ctx.getPath() + "/VarName:" + ctx.Identifier());
        ctx.setPath(ctx.getParent().getPath() + "/VarDecl/Type");
        return visitChildren(ctx);
    }

    @Override public Klass visitType(@NotNull MinijavaParser.TypeContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + ":" +ctx.getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }


    @Override public Klass visitNestedStatement(@NotNull MinijavaParser.NestedStatementContext ctx) {
        ctx.setPath(ctx.getParent().getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitIfElseStatement(@NotNull MinijavaParser.IfElseStatementContext ctx) {

        ctx.setPath(ctx.getParent().getPath() + "/If-Then-Else");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitIfBlock(@NotNull MinijavaParser.IfBlockContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/IfBlock");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitElseBlock(@NotNull MinijavaParser.ElseBlockContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ElseBlock");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }


    @Override public Klass visitWhileStatement(@NotNull MinijavaParser.WhileStatementContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/While");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitWhileBlock(@NotNull MinijavaParser.WhileBlockContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/WhileBlock");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitPrintStatement(@NotNull MinijavaParser.PrintStatementContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Print");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitVariableAssignmentStatement(@NotNull MinijavaParser.VariableAssignmentStatementContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/VarAssign");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitArrayAssignmentStatement(@NotNull MinijavaParser.ArrayAssignmentStatementContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ArrayAssign");
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }

    @Override public Klass visitArrayAccessExpression(@NotNull MinijavaParser.ArrayAccessExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ArrayAccess");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/ArrayAccess/ArrayName");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/ArrayAccess/ArrayIndex");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitArrayLengthExpression(@NotNull MinijavaParser.ArrayLengthExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/DotLength");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getPath() + "/ArrayName");
        visit(ctx.expression());
        return null;
    }

    @Override public Klass visitMethodCallExpression(@NotNull MinijavaParser.MethodCallExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/MethodCall");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getPath() + "/Caller");
        visit(ctx.expression(0));
        System.out.println(ctx.getParent().getPath() + "/MethodCall/MethodName:" + ctx.Identifier().getText());
        List<Klass> parameterList = new ArrayList<Klass>();
        int paramCount = 0;
        System.out.println(ctx.getParent().getPath() + "/MethodCall/Params");
        for(MinijavaParser.ExpressionContext expCtx : ctx.expression().subList(1, ctx.expression().size())){
            ctx.setPath(ctx.getParent().getPath() + "/MethodCall/Params/Param" + paramCount);
            parameterList.add(visit(expCtx));
            paramCount++;
        }
        return null;
    }

    @Override public Klass visitNotExpression(@NotNull MinijavaParser.NotExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/NotExpr");
        System.out.println(ctx.getPath());
        visitChildren(ctx);
        return null;
    }

    @Override public Klass visitArrayInstantiationExpression(@NotNull MinijavaParser.ArrayInstantiationExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ArrayInstantiation");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getPath() + "/ArraySize");
        visit(ctx.expression());
        return null;
    }

    @Override public Klass visitObjectInstantiationExpression(@NotNull MinijavaParser.ObjectInstantiationExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/ObjectInstantiation");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getPath() + "/Type:" + ctx.Identifier().getText());
        System.out.println(ctx.getPath());
        return null;
    }

    @Override public Klass visitPowExpression(@NotNull MinijavaParser.PowExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:POW");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:POW/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:POW/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitMulExpression(@NotNull MinijavaParser.MulExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:MUL");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:MUL/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:MUL/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitAddExpression(@NotNull MinijavaParser.AddExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:ADD");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:ADD/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:ADD/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitSubExpression(@NotNull MinijavaParser.SubExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:SUB");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:SUB/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:SUB/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitLtExpression(@NotNull MinijavaParser.LtExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:LT");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:LT/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:LT/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitAndExpression(@NotNull MinijavaParser.AndExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + "/Op:AND");
        System.out.println(ctx.getPath());
        ctx.setPath(ctx.getParent().getPath() + "/Op:AND/lhs");
        visit(ctx.expression(0));
        ctx.setPath(ctx.getParent().getPath() + "/Op:AND/rhs");
        visit(ctx.expression(1));
        return null;
    }

    @Override public Klass visitIntLitExpression(@NotNull MinijavaParser.IntLitExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + ":" +ctx.getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }
    @Override public Klass visitBooleanLitExpression(@NotNull MinijavaParser.BooleanLitExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + ":" +ctx.getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }
    @Override public Klass visitThisExpression(@NotNull MinijavaParser.ThisExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + ":" +ctx.getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }
    @Override public Klass visitParenExpression(@NotNull MinijavaParser.ParenExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath());
        return visitChildren(ctx);
    }
    @Override public Klass visitIdentifierExpression(@NotNull MinijavaParser.IdentifierExpressionContext ctx) {
        ctx.setPath(ctx.getParent().getPath() + ":" +ctx.getText());
        System.out.println(ctx.getPath());
        return visitChildren(ctx);
    }
}