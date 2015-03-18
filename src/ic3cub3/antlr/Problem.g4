grammar Problem;

options{
	language=Java;
}

////
//Parser rules
////

program: (programBody EOF);

programBody: (includes compoundStatement);

closedCompoundStatement: (LCURLY compoundStatement RCURLY);

compoundStatement: statement*;

label: (IDENTIFIER COLON statement);

statement: label
| assignStatement SEMICOLON
| functionDeclaration
| varDeclaration SEMICOLON
| functionCall SEMICOLON
| ifStatement
| elseStatement
| whileStatement
 | closedCompoundStatement;

expression: (andExpression (OR andExpression)*);

andExpression: booleanExpression (AND booleanExpression)*;

booleanExpression: addExpression ((EQUAL | NOTEQUAL | SMALLER | GREATER | SMALLEREQ | GREATEREQ) addExpression)*
| (NOT operand);

addExpression: mulExpression ((PLUS | MINUS) mulExpression)*;

mulExpression: operand ((MULT | DIVIDE | MOD) operand )*;

operand: AMPERSAND? var
| NUMBER
| TEXT
| functionCall
| (LPAREN expression RPAREN)
| staticArray;

array: (LBRACKET expression? RBRACKET);

var: (IDENTIFIER array?);

varDeclaration: (type var assign?);

ifStatement: IF LPAREN expression RPAREN statement;

elseStatement: ELSE statement;

whileStatement: WHILE LPAREN expression RPAREN statement;

assignStatement: (var ASSIGN expression);

assign: ASSIGN expression;

staticArray: (LCURLY expression? (COMMA expression)* RCURLY);

type: (CONST? types MULT?);

types: INTEGER | CHAR | VOID;

functionDeclaration: (type var LPAREN argDec RPAREN statement);

functionCall: (var LPAREN expression? (COMMA expression)* RPAREN);

argDec: ((type var)? (COMMA type var)*);

includes: INCLUDEHEADER*;

	// Lexer rules
	
//TOKENS
COLON : ':';
SEMICOLON : ';';
LPAREN : '(';
RPAREN : ')';
COMMA : ',';
LCURLY : '{';
RCURLY : '}';
LBRACKET : '[';
RBRACKET : ']';
ASSIGN : '=';
PLUS : '+';
MINUS : '-';
DIVIDE : '/';
MULT : '*';
SMALLER : '<';
GREATER : '>';
SMALLEREQ : '<=';
GREATEREQ : '>=';
EQUAL : '==';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
NOT : '!';
QUOTE : '"';
SINGLEQUOTE : '\'';
UNDERSCORE : '_';
HASH : '#';
MOD : '%';
INTEGER : 'int';
IF : 'if';
VOID : 'void';
RETURN : 'return';
CHAR : 'char';
AMPERSAND : '&';
ELSE : 'else';
WHILE : 'while';
CONST : 'const';
NULL : 'null';
INCLUDE : 'include';

TEXT: QUOTE(~( '"' | '\n' | '\r' ))* QUOTE;

INCLUDEHEADER: HASH INCLUDE .*? '\n';

IDENTIFIER: (LETTER| UNDERSCORE)(LETTER| UNDERSCORE| DIGIT)*;

NUMBER:	MINUS? DIGIT+;

COMMENT:('//' .*? '\n') -> skip;

WS:(' '| '\t'| '\f'	| '\r'| '\n' | '\u000C')+ -> skip;

fragment DIGIT: ('0' .. '9');

fragment LOWER:	('a' .. 'z');

fragment UPPER:('A' .. 'Z');

fragment LETTER: LOWER | UPPER;
