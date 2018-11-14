/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.via.jpe.intlang.ast;

import dk.via.jpe.intlang.*;
/**
 *
 * @author Romain
 */
public class FunctionDeclaration 
extends Declaration
{
	public Address address;
	public Identifier name;
	public Declarations params;
	public Block block;
	public Expression retExp;


	public FunctionDeclaration(Identifier name, Declarations params, Block block, Expression retExp) {
		this.name = name;
		this.params=params;
		this.block = block;
		this.retExp = retExp;
	}
	public Object visit( Visitor v, Object arg )
	{
		return v.visitFunctionDeclaration( this, arg );
	}
}
