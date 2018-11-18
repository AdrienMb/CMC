/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.via.jpe.intlang.ast;

/**
 *
 * @author Romain
 */
public class CallTab 
    	extends Expression
{
	public Identifier name;
	public Expression arg;
	
	public VariableDeclaration decl;
	
	
	public CallTab( Identifier name, Expression arg )
	{
		this.name = name;
		this.arg = arg;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitCallTab( this, arg );
	}
}
