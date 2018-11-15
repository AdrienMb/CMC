/*
 * 02.10.2016 Minor edit
 * 15.10.2010 IParser and IScanner
 * 29.10.2009 New package structure
 * 22.10.2006 Original version
 */
 
 
package dk.via.jpe.intlang;


import dk.via.jpe.intlang.ast.*;

import javax.swing.*;


public class TestDriverChecker
{
	private static final String EXAMPLES_DIR = "d:\\usr\\undervisning\\CMC\\IntLang\\examples";
	
	
	public static void main( String args[] )
	{
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		
		if( fc.showOpenDialog( null ) == fc.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
			ParserOperatorPrecedence p = new ParserOperatorPrecedence( s );
			Checker c = new Checker();
		
			AST ast = (AST) p.parseProgram();
			c.check( (Program) ast );
		}
	}
}