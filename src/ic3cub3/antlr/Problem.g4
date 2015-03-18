grammar Problem;

@header {
  package ic3cub3.antlr_gen;
}

options{
	
}

////
//Parser rules
////

problem: package_decl;

package_decl: PACKAGE IDENTIFIER;

////
//Lexer rules
////

IDENTIFIER : CHAR_LOWERCASE (CHAR | NUMBER)*;
STRING: '"' .*? '"';

//characters, and differentiate between upper and lowercase
fragment CHAR: (CHAR_UPPERCASE | CHAR_LOWERCASE);
fragment CHAR_UPPERCASE: [A-Z];
fragment CHAR_LOWERCASE: [a-z];

INT: NUMBER+;
fragment NUMBER : [0-9] ;

//skip spaces, newlines, and tabs
WS : [ \r\t\n]+ -> skip;


////
//Tokens
////

//
//Java keywords
//
PACKAGE : 'package';
PUBLIC : 'public';
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
RBRACE : '(';
LBRACKET : '[';
RBRACKET : ']';
DOUBLEEQUALS : '==';
EQUALS : '=';
SEMICOLON: ';';

