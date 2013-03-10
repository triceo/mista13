grammar MRCPSP;
 
@header {
package org.optaplanner.examples.mista2013.persistence.parsers.project;

import java.util.List;
import java.util.LinkedList;
}

@lexer::header {
package org.optaplanner.examples.mista2013.persistence.parsers.project;
}

@members {

  int numberOfResourcesToLookFor;

  public Integer toInteger(Token t) {
    return Integer.valueOf(t.getText());
  }
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

parse  returns [RawProjectData value]: 
  BEGIN_PAUSE sourceInfo 
  PAUSE summary 
  PAUSE projects 
  PAUSE precedence 
  PAUSE requestsAndDurations 
  PAUSE resourceAvailabilities
  { value = new RawProjectData($summary.value, $projects.value, $precedence.value, $requestsAndDurations.value, $resourceAvailabilities.value);} 
  PAUSE;

sourceInfo: 
  'file with basedata' WHITESPACE  ':' WHITESPACE STRING NEWLINE
  'initial value random generator' ':' WHITESPACE number;

summary returns [SituationMetadata value]:
  'projects' WHITESPACE                  ':' WHITESPACE pn=number NEWLINE
  'jobs (incl. supersource/sink )'       ':' WHITESPACE jn=number NEWLINE
  'horizon' WHITESPACE                   ':' WHITESPACE hz=number NEWLINE
  'RESOURCES' NEWLINE
  WHITESPACE '- renewable' WHITESPACE             ':' WHITESPACE rr=number WHITESPACE 'R' NEWLINE
  WHITESPACE '- nonrenewable' WHITESPACE          ':' WHITESPACE nr=number WHITESPACE 'N' NEWLINE
  WHITESPACE '- doubly constrained' WHITESPACE    ':' WHITESPACE dr=number WHITESPACE 'D'
  { 
    value = new SituationMetadata($pn.value, $jn.value, $hz.value, $rr.value, $nr.value, $dr.value);
    numberOfResourcesToLookFor = $rr.value + $nr.value + $dr.value;
  };

projects returns [ProjectMetadata value]:
  'PROJECT INFORMATION:' NEWLINE
  'pronr.  #jobs rel.date duedate tardcost  MPM-Time' NEWLINE 
  WHITESPACE nr=number 
  WHITESPACE jb=number 
  WHITESPACE rd=number 
  WHITESPACE dd=number
  WHITESPACE tc=number
  WHITESPACE mt=number
  { value = new ProjectMetadata($nr.value, $jb.value, $rd.value, $dd.value, $tc.value, $mt.value);};

precedence returns [List<Precedence> value]:
  'PRECEDENCE RELATIONS:' NEWLINE
  'jobnr.    #modes  #successors   successors'
  {value = new ArrayList<Precedence>();}
  (precedenceLine {value.add($precedenceLine.value);})+;
  
precedenceLine returns [Precedence value]: 
  NEWLINE 
  WHITESPACE jn=number
  WHITESPACE md=number
  WHITESPACE sc=number
  { List<Integer> successors = new ArrayList<Integer>(); } 
  (WHITESPACE ss=number {successors.add($ss.value);})*
  { value = new Precedence($jn.value, $md.value, $sc.value, successors);}
  (WHITESPACE)*;

requestsAndDurations returns [List<Request> value]:
  {value = new ArrayList<Request>();}
  'REQUESTS/DURATIONS:' NEWLINE
  'jobnr. mode duration  R 1  R 2  N 1  N 2' NEWLINE
  ('-')+
  {int jobNumber = 1;}
  (
  NEWLINE 
    { List<Integer> resources = new ArrayList<Integer>(); } 
    (WHITESPACE rr=number {resources.add($rr.value);})+
    {
      /* the file format is so stupid that we must first load all the numbers 
        and only after that decide which mean what. */
      Request r = null;
      int totalResources = numberOfResourcesToLookFor + 3;
      if (resources.size() == totalResources) {
        jobNumber = resources.get(0);
        r = new Request(jobNumber, resources.get(1), resources.get(2), resources.subList(3, resources.size()));
      } else {
        r = new Request(jobNumber, resources.get(0), resources.get(1), resources.subList(2, resources.size()));
      }
      value.add(r);
    }
  )+;
  
resourceAvailabilities returns [List<Integer> value]:
  {value = new ArrayList<Integer>();}
  'RESOURCEAVAILABILITIES:' NEWLINE
  .+ NEWLINE
  (WHITESPACE number {value.add($number.value);})+;

number returns [Integer value]: NUMBER {value = toInteger($NUMBER);};
 
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
