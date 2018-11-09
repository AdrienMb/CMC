package dk.via.jpe.intlang.ast;

import java.util.Vector;

public class FunctionDeclarations extends AST {
	public Vector<FunctionDeclaration> dec = new Vector<FunctionDeclaration>();

	@Override
	public Object visit( Visitor v, Object arg )
	{
		return v.visitFunctionDeclarations( this, arg );
	}

}
