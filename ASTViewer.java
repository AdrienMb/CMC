/*
 * 27.09.2016 Minor edit
 * 08.10.2010 Original Version
 */

package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.Operator;
import dk.via.jpe.intlang.ast.UnaryExpression;
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

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;


public class ASTViewer
extends JFrame
{
	private static final Font NODE_FONT = new Font( "Verdana", Font.PLAIN, 24 );


	public ASTViewer( AST ast )
	{
		super( "Abstract Syntax Tree" );

		DefaultMutableTreeNode root = createTree( ast );
		JTree tree = new JTree( root );
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setFont( NODE_FONT );
		tree.setCellRenderer( renderer );

		add( new JScrollPane( tree ) );

		setSize( 1024, 768 );
		setVisible( true );

		setDefaultCloseOperation( EXIT_ON_CLOSE );
	}


	private DefaultMutableTreeNode createTree( AST ast )
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode( "*** WHAT??? ***" );

		if( ast == null ) {
			node.setUserObject( "*** NULL ***" );
		}
		else if( ast instanceof Program ) {
			node.setUserObject( "Program" );
			node.add( createTree( ((Program)ast).block ) );
		} else if( ast instanceof Block ) {
			node.setUserObject( "Block" );
			node.add( createTree( ((Block)ast).decs ) );
			node.add( createTree( ((Block)ast).funcs ) );
			node.add( createTree( ((Block)ast).stats ) );
		} else if( ast instanceof Declarations ) {
			node.setUserObject( "Declarations" );

			for( Declaration d: ((Declarations)ast).dec )
				node.add( createTree( d ) );
		} else if( ast instanceof VariableDeclaration ) {
			node.setUserObject( "VariableDeclaration" );
			node.add( createTree( ((VariableDeclaration)ast).id ) );

		} else if( ast instanceof FunctionDeclaration ) {
			node.setUserObject( "FunctionDeclaration" );
			node.add( createTree( ((FunctionDeclaration)ast).name ) );
			node.add( createTree( ((FunctionDeclaration)ast).block ) );
			node.add( createTree( ((FunctionDeclaration)ast).retExp ) );
		} else if( ast instanceof Statements ) {
			node.setUserObject( "Statements" );

			for( Statement s: ((Statements)ast).stat )
				node.add( createTree( s ) );
		} else if( ast instanceof ExpressionStatement ) {
			node.setUserObject( "ExpressionStatement" );
			node.add( createTree( ((ExpressionStatement)ast).exp ) );
		} else if( ast instanceof IfStatement ) {
			node.setUserObject( "IfStatement" );
			node.add( createTree( ((IfStatement)ast).exp ) );
			node.add( createTree( ((IfStatement)ast).thenPart ) );
			node.add( createTree( ((IfStatement)ast).elsePart ) );
		} else if( ast instanceof WhileStatement ) {
			node.setUserObject( "WhileStatement" );
			node.add( createTree( ((WhileStatement)ast).exp ) );
			node.add( createTree( ((WhileStatement)ast).stats ) );
		} else if( ast instanceof DisplayStatement ) {
			node.setUserObject( "DisplayStatement" );
			node.add(createTree(((DisplayStatement)ast).exp ) );
		} else if( ast instanceof BinaryExpression ) {
			node.setUserObject( "BinaryExpression" );
			node.add( createTree( ((BinaryExpression)ast).operator ) );
			node.add( createTree( ((BinaryExpression)ast).operand1 ) );
			node.add( createTree( ((BinaryExpression)ast).operand2 ) );
		} else if( ast instanceof CallExpression ) {
			node.setUserObject( "CallExpression" );
			node.add( createTree( ((CallExpression)ast).name ) );
			node.add( createTree( ((CallExpression)ast).args ) );
                } else if( ast instanceof CallTab ) {
			node.setUserObject( "CallTab" );
			node.add( createTree( ((CallTab)ast).name ) );
			node.add( createTree( ((CallTab)ast).arg ) );        
		} else if( ast instanceof TabList ) {
			node.setUserObject( "TabList" );
			node.add( createTree( ((TabList)ast).args ) );        
		} else if( ast instanceof IntLitExpression ) {
			node.setUserObject( "IntLitExpression" );
			node.add( createTree( ((IntLitExpression)ast).literal ) );
		} else if( ast instanceof UnaryExpression ) {
			node.setUserObject( "UnaryExpression" );
			node.add( createTree( ((UnaryExpression)ast).operator ) );
			node.add( createTree( ((UnaryExpression)ast).operand ) );
		} else if( ast instanceof VarExpression ) {
			node.setUserObject( "VarExpression" );
			node.add( createTree( ((VarExpression)ast).name ) );
		} else if( ast instanceof ExpList ) {
			node.setUserObject( "ExpList" );

			for( Expression e: ((ExpList)ast).exp )
				node.add( createTree( e ) );
		} else if( ast instanceof Identifier ) {
			node.setUserObject( "Identifier " + ((Identifier)ast).spelling );
		} else if( ast instanceof IntegerLiteral ) {
			node.setUserObject( "IntegerLiteral " + ((IntegerLiteral)ast).spelling );
		} else if( ast instanceof Operator ) {
			node.setUserObject( "Operator " + ((Operator)ast).spelling );
		}

		return node;
	}
}