/*
 * 13.09.2016 IParse gone, IScanner gone, minor editing
 * 11.10.2010 Small fix
 * 01.10.2010 Renamed
 * 21.10.2009 New folder structure
 * 24.10.2006 params in function declaration empty Declarations instead of null
 * 23.10.2006 IdList replaced by Declarations for function parameters
 * 22.10.2006 Error in parsePrimary fixed
 * 01.10.2006 AST introduced
 * 28.09.2006 Original version (based on Watt&Brown)
 */

package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.FunctionDeclaration;
import dk.via.jpe.intlang.ast.UnaryExpression;
import dk.via.jpe.intlang.ast.Operator;
import dk.via.jpe.intlang.ast.Identifier;
import dk.via.jpe.intlang.ast.WhileStatement;
import dk.via.jpe.intlang.ast.Program;
import dk.via.jpe.intlang.ast.BinaryExpression;
import dk.via.jpe.intlang.ast.Declaration;
import dk.via.jpe.intlang.ast.IntLitExpression;
import dk.via.jpe.intlang.ast.Statements;
import dk.via.jpe.intlang.ast.Declarations;
import dk.via.jpe.intlang.ast.ExpList;
import dk.via.jpe.intlang.ast.IntegerLiteral;
import dk.via.jpe.intlang.ast.Block;
import dk.via.jpe.intlang.ast.CallExpression;
import dk.via.jpe.intlang.ast.DisplayStatement;
import dk.via.jpe.intlang.ast.Statement;
import dk.via.jpe.intlang.ast.Expression;
import dk.via.jpe.intlang.ast.VariableDeclaration;
import dk.via.jpe.intlang.ast.VarExpression;
import dk.via.jpe.intlang.ast.AST;
import dk.via.jpe.intlang.ast.IfStatement;
import dk.via.jpe.intlang.ast.ExpressionStatement;
import dk.via.jpe.intlang.ast.*;


public class ParserOperatorPrecedence
{
	private Scanner scan;


	private Token currentTerminal;


	public ParserOperatorPrecedence( Scanner scan )
	{
		this.scan = scan;

		currentTerminal = scan.scan();
	}


	public AST parseProgram()
	{
		Block block = parseBlock();

		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program");

		return new Program( block );
	}


	private Block parseBlock()
	{
		accept( Token.QUOTE );
		accept( Token.VARIABLES );
		accept( Token.COLONS );
		Declarations decs = parseDeclarations();

		Declarations funcs=null;
		if(currentTerminal.kind == Token.FUNCTIONS  ) {
			accept( Token.FUNCTIONS );
			accept( Token.COLONS );
			funcs = parseFunctions();
		}
		accept( Token.EXECUTE );
		accept( Token.COLONS );
		Statements stats = parseStatements();
		accept( Token.QUOTE );

		return new Block( decs, stats, funcs );
	}


	private Declarations parseDeclarations()
	{
		Declarations decs = new Declarations();
		removeNewLine();
		while( currentTerminal.kind == Token.NEW  )
			decs.dec.add( parseOneDeclaration() );

		return decs;
	}


	private Declaration parseOneDeclaration()
	{
		if( currentTerminal.kind==Token.NEW ) {
			accept( Token.NEW );
			if(currentTerminal.kind==Token.INT) {
				accept( Token.INT );
				Identifier id = parseIdentifier();
				accept( Token.NEWLINE );

				return new VariableDeclaration( id );
			}
			else if(currentTerminal.kind==Token.BOOLEAN) {
				accept( Token.BOOLEAN );
				Identifier id = parseIdentifier();
				accept( Token.NEWLINE );

				return new VariableDeclaration( id );
			}
			else if(currentTerminal.kind==Token.TAB) {
				accept( Token.TAB );
				Identifier id = parseIdentifier();
				accept( Token.NEWLINE );

				return new VariableDeclaration( id );
			}
			else {
				System.out.println( "int, boolean or tab expected" );
				return null;
			}
		}
		else {
			System.out.println( "new expected" );
			return null;
		}
	}

	private Declarations parseFunctions()
	{
		Declarations funcs = new Declarations();

		removeNewLine();
		while( currentTerminal.kind == Token.NEW )
			funcs.dec.add( parseOneFunction() );

		return funcs;
	}


	private FunctionDeclaration parseOneFunction()
	{
		if( currentTerminal.kind==Token.NEW ) {
			accept( Token.NEW );
			Identifier name = parseIdentifier();
			accept( Token.COLONS );
			Block block = parseBlock();
			accept( Token.NEWLINE );
			accept( Token.RESULT );
			Expression retExp = parseExpression();
			accept( Token.NEWLINE );

			return new FunctionDeclaration(name, block.decs, block, retExp);
		}

		else {
			System.out.println( "var or func expected" );
			return null;
		}
	}




	/*private void parseIdList()
	{
		accept( Token.IDENTIFIER );

		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			accept( Token.IDENTIFIER );
		}
	} */


	private Statements parseStatements()
	{

		Statements stats = new Statements();

		removeNewLine();
		while( currentTerminal.kind == Token.IDENTIFIER ||
				currentTerminal.kind == Token.OPERATOR ||
				currentTerminal.kind == Token.INTEGERLITERAL ||
				currentTerminal.kind == Token.LEFTPARAN ||
				currentTerminal.kind == Token.IF ||
				currentTerminal.kind == Token.WHILE ||
				currentTerminal.kind==Token.DISPLAY)
			stats.stat.add( parseOneStatement() );

		return stats;
	}


	private Statement parseOneStatement()
	{
		switch( currentTerminal.kind ) {
		case Token.IDENTIFIER:
		case Token.INTEGERLITERAL:
		case Token.OPERATOR:
		case Token.LEFTPARAN:
			Expression exp = parseExpression();
			accept(Token.NEWLINE);
			return new ExpressionStatement( exp );

		case Token.IF:
			accept( Token.IF );
			Expression ifExp = parseExpression();
			accept( Token.DO );
			accept( Token.COLONS);
			accept( Token.QUOTE );
			Statements thenPart = parseStatements();
			accept( Token.QUOTE );
			removeNewLine();
			Statements elsePart = null;
			if( currentTerminal.kind == Token.ELSE ) {
				accept( Token.ELSE );
				accept( Token.COLONS );
				accept( Token.QUOTE );
				elsePart = parseStatements();
				accept( Token.QUOTE );
			}
			removeNewLine();
			return new IfStatement( ifExp, thenPart, elsePart );

		case Token.WHILE:
			accept( Token.WHILE );
			Expression whileExp = parseExpression();
			accept( Token.DO );
			accept( Token.COLONS );
			accept( Token.QUOTE );
			Statements stats = parseStatements();
			accept( Token.QUOTE );
			accept(Token.NEWLINE);

			return new WhileStatement( whileExp, stats );

		case Token.DISPLAY:
			accept( Token.DISPLAY);
			Expression displayExp = parseExpression();
			accept( Token.NEWLINE );

			return new DisplayStatement( displayExp );

		default:
			System.out.println( "Error in statement" );
			return null;
		}
	}


	private Expression parseExpression()
	{
		Expression res = parseExpression1();
		if( currentTerminal.isAssignOperator() || currentTerminal.kind==Token.QUESTION) {
			if(currentTerminal.kind==Token.QUESTION) {
				accept(Token.QUESTION);}
			else {                              
				Operator op = parseOperator();
				Expression tmp = parseExpression();
				res = new BinaryExpression( op, res, tmp );
			}
		}
		return res;
	}
        
        private Expression parseExpression1()
	{
		Expression res = parsePrimary();
		while( currentTerminal.isAddOperator() ||  currentTerminal.isCompareOperator() || currentTerminal.kind==Token.QUESTION) {
			if(currentTerminal.kind==Token.QUESTION) {
				accept(Token.QUESTION);}
			else { 
                                System.out.println("add or compare");
				Operator op = parseOperator();
				Expression tmp = parseExpression2();
				res = new BinaryExpression( op, res, tmp );
			}
		}
		return res;
	}
        
        private Expression parseExpression2()
	{
		Expression res = parsePrimary();
		while( currentTerminal.isMulOperator() || currentTerminal.kind==Token.QUESTION) {
			if(currentTerminal.kind==Token.QUESTION) {
				accept(Token.QUESTION);}
			else {                              
				Operator op = parseOperator();
				Expression tmp = parsePrimary();
				res = new BinaryExpression( op, res, tmp );
			}
		}
		return res;
	}
        
        


	private Expression parsePrimary()
	{
		switch( currentTerminal.kind ) {
		case Token.IDENTIFIER:
			Identifier name = parseIdentifier();

			if( currentTerminal.kind == Token.LEFTPARAN ) {
				accept( Token.LEFTPARAN );

				ExpList args;

				if( currentTerminal.kind == Token.IDENTIFIER ||
						currentTerminal.kind == Token.INTEGERLITERAL ||
						currentTerminal.kind == Token.OPERATOR ||
						currentTerminal.kind == Token.LEFTPARAN )
					args = parseExpressionList();
				else
					args = new ExpList();



				accept( Token.RIGHTPARAN );
				return new CallExpression( name, args );
			}
			else
				return new VarExpression( name );

		case Token.INTEGERLITERAL:
			IntegerLiteral lit = parseIntegerLiteral();

			return new IntLitExpression( lit );

		case Token.OPERATOR:
			Operator op = parseOperator();
			Expression exp = parsePrimary();
			return new UnaryExpression( op, exp );

		case Token.LEFTPARAN:
			accept( Token.LEFTPARAN);
			ExpList args;

			if( currentTerminal.kind == Token.IDENTIFIER ||
					currentTerminal.kind == Token.INTEGERLITERAL ||
					currentTerminal.kind == Token.OPERATOR ||
					currentTerminal.kind == Token.LEFTPARAN )
				args = parseExpressionList();
			else
				args = new ExpList();


			accept( Token.RIGHTPARAN );

			return new TabList( args );

			/*case Token.QUESTION:
			accept(Token.QUESTION);
			if (currentTerminal.kind==Token.QUOTE)
				accept( Token.QUOTE );
			accept( Token.NEWLINE );
			break;*/

			default:
				System.out.println( "Error in primary" );
				return null;
		}
	}


	private ExpList parseExpressionList()
	{
		ExpList exps = new ExpList();

		removeNewLine();
		exps.exp.add( parseExpression() );
		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			exps.exp.add( parseExpression() );
		}

		return exps;
	}

	private Identifier parseIdentifier()
	{
		if( currentTerminal.kind == Token.IDENTIFIER ) {
			Identifier res = new Identifier( currentTerminal.spelling );
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println( "Identifier expected" );

			return new Identifier( "???" );
		}
	}

	private IntegerLiteral parseIntegerLiteral()
	{
		if( currentTerminal.kind == Token.INTEGERLITERAL ) {
			IntegerLiteral res = new IntegerLiteral( currentTerminal.spelling );
			currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println( "Integer literal expected" );

			return new IntegerLiteral( "???" );
		}
	}


	private Operator parseOperator()
	{
		if( currentTerminal.kind == Token.OPERATOR ) {
			Operator res = new Operator( currentTerminal.spelling );
                        System.out.println("op : "+res.spelling);
                        currentTerminal = scan.scan();

			return res;
		} else {
			System.out.println( "Operator expected" );

			return new Operator( "???" );
		}
	}


	private void accept( byte expected )
	{
		System.out.println(currentTerminal.spelling);
		while (currentTerminal.kind == Token.NEWLINE && currentTerminal.kind != expected) {
			currentTerminal = scan.scan();
		}
		if( currentTerminal.kind == expected ) 
			currentTerminal = scan.scan();

		else
			System.out.println( "Expected token of kind " + expected + " instead of "+currentTerminal.kind);
	}


	private void removeNewLine() {
		while (currentTerminal.kind == Token.NEWLINE) 
			currentTerminal = scan.scan();
	}
}