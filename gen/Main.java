package gen;

import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import java.io.*;
import java.util.*;

/**
 *The driver class for this minijava compiler.
 *@author Chaskin Saroff
 */
public class Main{

    /**
     * The name of the input file that is being parsed.
     */
	private static String inputFile = null;

    /**
     * Compiles minijava programs, reporting syntax and semantic errors.
     * @param  args                  The name of the input file to be parsed
     * @throws IOException           If an IO error occurs while parsing the file.
     * @throws FileNotFoundException If the specified file does not exist.
     */
	public static void main(String[] args) throws IOException, FileNotFoundException {
        //The file input stream for lexing the file.  
		InputStream is = null;

        if ( args.length>0 ){
            //Assign the first argument of the program as the inputFile name
            inputFile = args[0];
            is = new FileInputStream(inputFile);
        }

        //A wrapper of the file input stream for lexing the file.
        ANTLRInputStream input = new ANTLRInputStream(is);

        //A lexer object for lexing(tokenizing) the file input stream.
        MinijavaLexer lexer = new MinijavaLexer(input);

        //A stream view of the tokenized file, built by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //The parser taking the token stream for parse tree construction.
        MinijavaParser parser = new MinijavaParser(tokens);

        //A symbol-table representation of minijava classes for use in semantic analysis.
        Map<String, Klass> klasses = new HashMap<String, Klass>();

        //A collection of the symbol-table scopes of the minijava program.
        //A ParseTreeProperty<T> is a map from a particular parse tree node, to T.
        ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();

        //The type of the left hand side of a method call expression.
        //ex String stackToString = (new Stack()).toString();
        //To the left of this dot has type Stack ^
        ParseTreeProperty<Klass> callerTypes = new ParseTreeProperty<Klass>();
        
        //label

        //Remove default error listeners to ensure that error messages are not repeated.
		parser.removeErrorListeners(); // remove ConsoleErrorListener

        //Reports ambiguities or errors in the grammar that have passed Antlr's static analysis of the grammar phase.
        //http://www.antlr.org/api/Java/org/antlr/v4/runtime/DiagnosticErrorListener.html
        parser.addErrorListener(new DiagnosticErrorListener());
		parser.getInterpreter()
		.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);

        //Reports syntax errors upon construction of the parse tree.
        //Underlines the offending token and prints the follows set of
        //(The set of all tokens that can legally follow) the previous token.
		parser.addErrorListener(new UnderlineListener());

        //Construct the parse tree, reporting all syntax errors as stated above.
		ParseTree tree = parser.goal();

        //Convert CST(Parse Tree) to AST(Abstract syntax tree)
        //Simply use visitor pattern and print the AST on the console.
        BuildAST buildAST = new BuildAST();
        buildAST.visit(tree);

        //If errors were encountered during parsing, print them and stop compiling.
        ErrorPrinter.exitOnErrors();
        //A listener for naming the classes in the symbol table
        ClassNamer namer = new ClassNamer(klasses, parser); 
        //Traverse the parse tree, creating klasses and naming them.
        ParseTreeWalker.DEFAULT.walk(namer, tree);

        //If errors were encountered during naming 
        //eg. Defining two classes with the same name,
        //print the error count and stop compiling.
        ErrorPrinter.exitOnErrors();        
        //A listener for building the symbol table. 
        AssignmentListener assigner = new AssignmentListener(klasses, scopes, parser);
        //Traverse the parse tree, filling the symbol table.
        ParseTreeWalker.DEFAULT.walk(assigner, tree); 
        
        //If errors were encountered during naming 
        //eg. Cyclic Inheritance (class A extends B and class B extends A)
        //print the error count and stop compiling.
        ErrorPrinter.exitOnErrors();        
        //A visitor for type checking the program
        TypeChecker typeChecker = new TypeChecker(klasses, scopes, callerTypes, parser);
        //Traverse the parse tree, printing type check errors
        typeChecker.visit(tree);
        
        //If errors were encountered during naming 
        //eg int x;          
        //x = 0;
        //x = new int[] + x; //Error, + takes two ints, but int[],int was found
        //print the error count and stop compiling.
        ErrorPrinter.exitOnErrors();        
        //A visitor for ensuring that a variable was initialized before it was used.
        InitializationBeforeUseChecker iBeforeUChecker = new InitializationBeforeUseChecker(klasses, scopes, parser);
        //Traverse the parse tree, printing initialization before use errors.
        iBeforeUChecker.visit(tree);
        

	}
    /**
     * A method for returning the filename of the file that is being parsed.
     * @return the filename.
     */
    public static String getFileName(){
    	return inputFile;
    }
}
