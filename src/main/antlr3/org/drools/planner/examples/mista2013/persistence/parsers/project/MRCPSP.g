grammar MRCPSP;
 
@header {
package org.drools.planner.examples.mista2013.persistence.parsers.project;
}

@lexer::header {
package org.drools.planner.examples.mista2013.persistence.parsers.project;
}

@members {
  // java code here
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

parse : BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE;

sourceInfo: 
  'file with basedata' WHITESPACE  ':' WHITESPACE STRING NEWLINE
  'initial value random generator' ':' WHITESPACE number;

summary:
  'projects' WHITESPACE                  ':' WHITESPACE number NEWLINE
  'jobs (incl. supersource/sink )'       ':' WHITESPACE number NEWLINE
  'horizon' WHITESPACE                   ':' WHITESPACE number NEWLINE
  'RESOURCES' NEWLINE
  WHITESPACE '- renewable' WHITESPACE             ':' WHITESPACE number WHITESPACE 'R' NEWLINE
  WHITESPACE '- nonrenewable' WHITESPACE          ':' WHITESPACE number WHITESPACE 'N' NEWLINE
  WHITESPACE '- doubly constrained' WHITESPACE    ':' WHITESPACE number WHITESPACE 'D' ;

projects:
  'PROJECT INFORMATION:' NEWLINE
  'pronr.  #jobs rel.date duedate tardcost  MPM-Time' NEWLINE 
  WHITESPACE number 
  WHITESPACE number 
  WHITESPACE number 
  WHITESPACE number
  WHITESPACE number
  WHITESPACE number;

precedence:
  'PRECEDENCE RELATIONS:' NEWLINE
  'jobnr.    #modes  #successors   successors'
  (precedenceLine)+;
  
precedenceLine: 
  NEWLINE 
  WHITESPACE number 
  WHITESPACE number 
  WHITESPACE number 
  (WHITESPACE number)*
  (WHITESPACE)*;

requestsAndDurations:
  'REQUESTS/DURATIONS:' NEWLINE
  'jobnr. mode duration  R 1  R 2  N 1  N 2' NEWLINE
  ('-')+
  (requestLine)+;
  
requestLine:
  NEWLINE 
  (WHITESPACE number | WHITESPACE) 
  WHITESPACE number 
  WHITESPACE number 
  (WHITESPACE number)+;
  
resourceAvailabilities:
  'RESOURCEAVAILABILITIES:' NEWLINE
  .+ NEWLINE
  (WHITESPACE number)+;

number: NUMBER;
 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

NUMBER : DIGIT+;
 
WHITESPACE : ( '\t' | ' ' )+ ;
 
NEWLINE : ( '\r' | '\n' )+ ;

BEGIN_PAUSE : ('*')+ NEWLINE;

PAUSE : NEWLINE BEGIN_PAUSE;

STRING : ALPHABET (ALPHABET | DIGIT | '-' | '_' | '.' )*;

fragment DIGIT: '0'..'9';
fragment ALPHABET: 'a' .. 'z' | 'A' .. 'Z';
