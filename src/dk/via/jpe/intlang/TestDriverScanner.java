/*
 * 16.08.2016 IScanner gone, minor editing
 * 21.09.2012 Examples Directory Changed
 * 20.09.2010 IScanner
 * 25.09.2009 New package structure
 * 21.09.2007 File Chooser
 * 22.09.2006 Original version
 */
 
package dk.via.jpe.intlang;


import javax.swing.*;

 
public class TestDriverScanner
{
	private static final String EXAMPLES_DIR = "C:\\Users\\adrie\\Documents\\VIA\\CMC\\EZ.txt";
	
	
	public static void main( String args[] )
	{
		JFileChooser fc = new JFileChooser( EXAMPLES_DIR );
		
		if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			SourceFile in = new SourceFile( fc.getSelectedFile().getAbsolutePath() );
			Scanner s = new Scanner( in );
		
			Token t = s.scan();
			while( t.kind != Token.EOT ) {
				System.out.println( t.kind + " " + t.spelling );
			
				t = s.scan();
			}
		}
	}
}