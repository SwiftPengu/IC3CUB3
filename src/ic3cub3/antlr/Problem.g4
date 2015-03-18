grammar Problem;



////
//Parser rules
////

problem: package_decl import_decl* classdef EOF;

package_decl: PACKAGE IDENTIFIER (DOT IDENTIFIER)* SEMICOLON;

import_decl: IMPORT IDENTIFIER (DOT (IDENTIFIER | classname))* SEMICOLON;

classfielddef: randomclassdef | bufferedreaderdef;

var_decl : (randomclassdef | bufferedreaderdef);
method_decl : DOT;

classdef: PUBLIC CLASS IDENTIFIER LPAREN (
		var_decl |
		method_decl
	) RPAREN;
	
//static class definitions which are present in every problem
randomclassdef: PUBLIC CNRANDOM IDENTIFIER EQUALS NEW CNRANDOM LBRACE RBRACE SEMICOLON;
bufferedreaderdef: STATIC CNBUFFR IDENTIFIER EQUALS NEW CNBUFFR;

classname: (CNRANDOM | CNBUFFR);

////
//Lexer rules
////


//
//Java keywords
//
PACKAGE : 'package';
IMPORT : 'import';
CLASS : 'class';
PUBLIC : 'public';
PRIVATE : 'private';
STATIC : 'static';
VOID : 'void';
MAIN : 'main';
NEW : 'new';
THROW : 'throw';
TRY : 'try';
CATCH : 'catch';
TYPE_INT : 'int';
TYPE_STR : 'String'; 

ARRAY_DECL: '[]';

IF : 'if';

//constants
TRUE: 'true';
FALSE: 'false';

//characters
LPAREN : '{';
RPAREN : '}';
LBRACE : '(';
RBRACE : ')';
LBRACKET : '[';
RBRACKET : ']';
DOUBLEEQUALS : '==';
EQUALS : '=';
BINARYAND : '&&';
BITWISEAND : '&';
FORWARDSLASH : '/';
BACKWARDSLASH : '\\';
SEMICOLON: ';';
MINUS : '-';
PLUS : '+';
COMMA : ',';
DOT : '.';

//auxiliary names
CNRANDOM : 'Random';
CNBUFFR : 'BufferedReader';

//base tokens

IDENTIFIER : CHAR (CHAR | NUMBER)*;
STRING: '"' .*? '"';

//characters, and differentiate between upper and lowercase
fragment CHAR: (CHAR_UPPERCASE | CHAR_LOWERCASE);
fragment CHAR_UPPERCASE: [A-Z];
fragment CHAR_LOWERCASE: [a-z];

INT: NUMBER+;
fragment NUMBER : [0-9] ;

//skip spaces, newlines, and tabs
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip ;
//skip comments
COMMENT : '//' (.)*? ('\n' | '\t') -> skip;

