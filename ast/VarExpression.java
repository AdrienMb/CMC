/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 08.10.2010 name made public
 * 21.10.2009 New folder structure
 * 01.10.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class VarExpression
	extends Expression
{
	public Identifier name;
	public VariableDeclaration decl;
	
	
	public VarExpression( Identifier name )
	{
		this.name = name;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitVarExpression( this, arg );
	}
}