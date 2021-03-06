/*
 * 27.09.2016 Minor edit
 * 11.10.2010 dump() removed
 * 21.10.2009 New folder structure
 * 29.09.2006 Original version
 */
 
package dk.via.jpe.intlang.ast;


public class DisplayStatement
	extends Statement
{
	public Expression exp;
	
	
	public DisplayStatement( Expression exp )
	{
		this.exp = exp;
	}
	public Object visit( Visitor v, Object arg )
	{
            return v.visitDisplayStatement( this, arg );
        }
}