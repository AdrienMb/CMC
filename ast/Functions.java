/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.via.jpe.intlang.ast;

import java.util.Vector;

/**
 *
 * @author Romain
 */
public class Functions 	
        extends AST
{
	public Vector<Function> func = new Vector<Function>();
	public Object visit( Visitor v, Object arg )
	{
		return v.visitFunctions( this, arg );
	}
}
