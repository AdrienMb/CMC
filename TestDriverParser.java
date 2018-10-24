/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 21.09.2012 Default directory changed
 * 24.10.2009 IScanner and IParser
 * 07.10.2009 New package structure
 * 28.09.2006 Original version
 */
  
package dk.via.jpe.intlang;


import javax.swing.*;


public class TestDriverParser
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
			Parser p = new Parser( s );
		
			p.parseProgram();
		}
	}
}