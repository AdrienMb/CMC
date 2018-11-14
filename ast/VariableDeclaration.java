/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 21.10.2009 New folder structure
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;

import dk.via.jpe.intlang.*;

public class VariableDeclaration
	extends Declaration
{
	public Identifier id;
	
	public Address adr;
	
	public VariableDeclaration( Identifier id )
	{
		this.id = id;
	}
	
	public Object visit( Visitor v, Object arg )
	{
		return v.visitVariableDeclaration( this, arg );
	}
}