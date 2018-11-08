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
public class FunctionDeclaration 
    	extends Function
{
	public Identifier name;
	public Block block;
	public Expression retExp;
	

        public FunctionDeclaration(Identifier name, Block block, Expression retExp) {
                this.name = name;
                this.block = block;
                this.retExp = retExp;
        }
}
