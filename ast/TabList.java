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
public class TabList 	
        extends Expression
{
	public ExpList args;
	
	
	public TabList( ExpList args )
	{
		this.args = args;
	}
}
