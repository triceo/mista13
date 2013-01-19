// $ANTLR 3.5 /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g 2013-01-19 20:40:49

package org.drools.planner.examples.mista2013.persistence.parsers.project;

import java.util.List;
import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MRCPSPParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHABET", "BEGIN_PAUSE", "DIGIT", 
		"NEWLINE", "NUMBER", "PAUSE", "STRING", "WHITESPACE", "'- doubly constrained'", 
		"'- nonrenewable'", "'- renewable'", "'-'", "':'", "'D'", "'N'", "'PRECEDENCE RELATIONS:'", 
		"'PROJECT INFORMATION:'", "'R'", "'REQUESTS/DURATIONS:'", "'RESOURCEAVAILABILITIES:'", 
		"'RESOURCES'", "'file with basedata'", "'horizon'", "'initial value random generator'", 
		"'jobnr.    #modes  #successors   successors'", "'jobnr. mode duration  R 1  R 2  N 1  N 2'", 
		"'jobs (incl. supersource/sink )'", "'projects'", "'pronr.  #jobs rel.date duedate tardcost  MPM-Time'"
	};
	public static final int EOF=-1;
	public static final int T__12=12;
	public static final int T__13=13;
	public static final int T__14=14;
	public static final int T__15=15;
	public static final int T__16=16;
	public static final int T__17=17;
	public static final int T__18=18;
	public static final int T__19=19;
	public static final int T__20=20;
	public static final int T__21=21;
	public static final int T__22=22;
	public static final int T__23=23;
	public static final int T__24=24;
	public static final int T__25=25;
	public static final int T__26=26;
	public static final int T__27=27;
	public static final int T__28=28;
	public static final int T__29=29;
	public static final int T__30=30;
	public static final int T__31=31;
	public static final int T__32=32;
	public static final int ALPHABET=4;
	public static final int BEGIN_PAUSE=5;
	public static final int DIGIT=6;
	public static final int NEWLINE=7;
	public static final int NUMBER=8;
	public static final int PAUSE=9;
	public static final int STRING=10;
	public static final int WHITESPACE=11;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MRCPSPParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MRCPSPParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return MRCPSPParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g"; }



	  int numberOfResourcesToLookFor;

	  public Integer toInteger(Token t) {
	    return Integer.valueOf(t.getText());
	  }



	// $ANTLR start "parse"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:28:1: parse returns [RawProjectData value] : BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE ;
	public final RawProjectData parse() throws RecognitionException {
		RawProjectData value = null;


		SituationMetadata summary1 =null;
		ProjectMetadata projects2 =null;
		List<Precedence> precedence3 =null;
		List<Request> requestsAndDurations4 =null;
		List<Integer> resourceAvailabilities5 =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:28:38: ( BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:29:3: BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE
			{
			match(input,BEGIN_PAUSE,FOLLOW_BEGIN_PAUSE_in_parse43); 
			pushFollow(FOLLOW_sourceInfo_in_parse45);
			sourceInfo();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse50); 
			pushFollow(FOLLOW_summary_in_parse52);
			summary1=summary();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse57); 
			pushFollow(FOLLOW_projects_in_parse59);
			projects2=projects();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse64); 
			pushFollow(FOLLOW_precedence_in_parse66);
			precedence3=precedence();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse71); 
			pushFollow(FOLLOW_requestsAndDurations_in_parse73);
			requestsAndDurations4=requestsAndDurations();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse78); 
			pushFollow(FOLLOW_resourceAvailabilities_in_parse80);
			resourceAvailabilities5=resourceAvailabilities();
			state._fsp--;

			 value = new RawProjectData(summary1, projects2, precedence3, requestsAndDurations4, resourceAvailabilities5);
			match(input,PAUSE,FOLLOW_PAUSE_in_parse89); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "parse"



	// $ANTLR start "sourceInfo"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:38:1: sourceInfo : 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number ;
	public final void sourceInfo() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:38:11: ( 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:39:3: 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number
			{
			match(input,25,FOLLOW_25_in_sourceInfo99); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo101); 
			match(input,16,FOLLOW_16_in_sourceInfo104); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo106); 
			match(input,STRING,FOLLOW_STRING_in_sourceInfo108); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_sourceInfo110); 
			match(input,27,FOLLOW_27_in_sourceInfo114); 
			match(input,16,FOLLOW_16_in_sourceInfo116); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo118); 
			pushFollow(FOLLOW_number_in_sourceInfo120);
			number();
			state._fsp--;

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "sourceInfo"



	// $ANTLR start "summary"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:42:1: summary returns [SituationMetadata value] : 'projects' WHITESPACE ':' WHITESPACE pn= number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE jn= number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE hz= number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE rr= number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE nr= number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE dr= number WHITESPACE 'D' ;
	public final SituationMetadata summary() throws RecognitionException {
		SituationMetadata value = null;


		Integer pn =null;
		Integer jn =null;
		Integer hz =null;
		Integer rr =null;
		Integer nr =null;
		Integer dr =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:42:42: ( 'projects' WHITESPACE ':' WHITESPACE pn= number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE jn= number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE hz= number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE rr= number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE nr= number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE dr= number WHITESPACE 'D' )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:43:3: 'projects' WHITESPACE ':' WHITESPACE pn= number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE jn= number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE hz= number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE rr= number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE nr= number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE dr= number WHITESPACE 'D'
			{
			match(input,31,FOLLOW_31_in_summary133); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary135); 
			match(input,16,FOLLOW_16_in_summary154); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary156); 
			pushFollow(FOLLOW_number_in_summary160);
			pn=number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary162); 
			match(input,30,FOLLOW_30_in_summary166); 
			match(input,16,FOLLOW_16_in_summary174); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary176); 
			pushFollow(FOLLOW_number_in_summary180);
			jn=number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary182); 
			match(input,26,FOLLOW_26_in_summary186); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary188); 
			match(input,16,FOLLOW_16_in_summary208); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary210); 
			pushFollow(FOLLOW_number_in_summary214);
			hz=number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary216); 
			match(input,24,FOLLOW_24_in_summary220); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary222); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary226); 
			match(input,14,FOLLOW_14_in_summary228); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary230); 
			match(input,16,FOLLOW_16_in_summary244); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary246); 
			pushFollow(FOLLOW_number_in_summary250);
			rr=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary252); 
			match(input,21,FOLLOW_21_in_summary254); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary256); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary260); 
			match(input,13,FOLLOW_13_in_summary262); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary264); 
			match(input,16,FOLLOW_16_in_summary275); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary277); 
			pushFollow(FOLLOW_number_in_summary281);
			nr=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary283); 
			match(input,18,FOLLOW_18_in_summary285); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary287); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary291); 
			match(input,12,FOLLOW_12_in_summary293); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary295); 
			match(input,16,FOLLOW_16_in_summary300); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary302); 
			pushFollow(FOLLOW_number_in_summary306);
			dr=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary308); 
			match(input,17,FOLLOW_17_in_summary310); 
			 
			    value = new SituationMetadata(pn, jn, hz, rr, nr, dr);
			    numberOfResourcesToLookFor = rr + nr + dr;
			  
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "summary"



	// $ANTLR start "projects"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:55:1: projects returns [ProjectMetadata value] : 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE nr= number WHITESPACE jb= number WHITESPACE rd= number WHITESPACE dd= number WHITESPACE tc= number WHITESPACE mt= number ;
	public final ProjectMetadata projects() throws RecognitionException {
		ProjectMetadata value = null;


		Integer nr =null;
		Integer jb =null;
		Integer rd =null;
		Integer dd =null;
		Integer tc =null;
		Integer mt =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:55:41: ( 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE nr= number WHITESPACE jb= number WHITESPACE rd= number WHITESPACE dd= number WHITESPACE tc= number WHITESPACE mt= number )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:56:3: 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE nr= number WHITESPACE jb= number WHITESPACE rd= number WHITESPACE dd= number WHITESPACE tc= number WHITESPACE mt= number
			{
			match(input,20,FOLLOW_20_in_projects327); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_projects329); 
			match(input,32,FOLLOW_32_in_projects333); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_projects335); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects340); 
			pushFollow(FOLLOW_number_in_projects344);
			nr=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects349); 
			pushFollow(FOLLOW_number_in_projects353);
			jb=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects358); 
			pushFollow(FOLLOW_number_in_projects362);
			rd=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects367); 
			pushFollow(FOLLOW_number_in_projects371);
			dd=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects375); 
			pushFollow(FOLLOW_number_in_projects379);
			tc=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects383); 
			pushFollow(FOLLOW_number_in_projects387);
			mt=number();
			state._fsp--;

			 value = new ProjectMetadata(nr, jb, rd, dd, tc, mt);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "projects"



	// $ANTLR start "precedence"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:66:1: precedence returns [List<Precedence> value] : 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+ ;
	public final List<Precedence> precedence() throws RecognitionException {
		List<Precedence> value = null;


		Precedence precedenceLine6 =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:66:44: ( 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:67:3: 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+
			{
			match(input,19,FOLLOW_19_in_precedence404); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_precedence406); 
			match(input,28,FOLLOW_28_in_precedence410); 
			value = new ArrayList<Precedence>();
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:70:3: ( precedenceLine )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==NEWLINE) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:70:4: precedenceLine
					{
					pushFollow(FOLLOW_precedenceLine_in_precedence419);
					precedenceLine6=precedenceLine();
					state._fsp--;

					value.add(precedenceLine6);
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "precedence"



	// $ANTLR start "precedenceLine"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:72:1: precedenceLine returns [Precedence value] : NEWLINE WHITESPACE jn= number WHITESPACE md= number WHITESPACE sc= number ( WHITESPACE ss= number )* ( WHITESPACE )* ;
	public final Precedence precedenceLine() throws RecognitionException {
		Precedence value = null;


		Integer jn =null;
		Integer md =null;
		Integer sc =null;
		Integer ss =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:72:42: ( NEWLINE WHITESPACE jn= number WHITESPACE md= number WHITESPACE sc= number ( WHITESPACE ss= number )* ( WHITESPACE )* )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:73:3: NEWLINE WHITESPACE jn= number WHITESPACE md= number WHITESPACE sc= number ( WHITESPACE ss= number )* ( WHITESPACE )*
			{
			match(input,NEWLINE,FOLLOW_NEWLINE_in_precedenceLine439); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine444); 
			pushFollow(FOLLOW_number_in_precedenceLine448);
			jn=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine452); 
			pushFollow(FOLLOW_number_in_precedenceLine456);
			md=number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine460); 
			pushFollow(FOLLOW_number_in_precedenceLine464);
			sc=number();
			state._fsp--;

			 List<Integer> successors = new ArrayList<Integer>(); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:78:3: ( WHITESPACE ss= number )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==WHITESPACE) ) {
					int LA2_1 = input.LA(2);
					if ( (LA2_1==NUMBER) ) {
						alt2=1;
					}

				}

				switch (alt2) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:78:4: WHITESPACE ss= number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine474); 
					pushFollow(FOLLOW_number_in_precedenceLine478);
					ss=number();
					state._fsp--;

					successors.add(ss);
					}
					break;

				default :
					break loop2;
				}
			}

			 value = new Precedence(jn, md, sc, successors);
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:80:3: ( WHITESPACE )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==WHITESPACE) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:80:4: WHITESPACE
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine491); 
					}
					break;

				default :
					break loop3;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "precedenceLine"



	// $ANTLR start "requestsAndDurations"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:82:1: requestsAndDurations returns [List<Request> value] : 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( NEWLINE ( WHITESPACE rr= number )+ )+ ;
	public final List<Request> requestsAndDurations() throws RecognitionException {
		List<Request> value = null;


		Integer rr =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:82:51: ( 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( NEWLINE ( WHITESPACE rr= number )+ )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:83:3: 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( NEWLINE ( WHITESPACE rr= number )+ )+
			{
			value = new ArrayList<Request>();
			match(input,22,FOLLOW_22_in_requestsAndDurations510); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_requestsAndDurations512); 
			match(input,29,FOLLOW_29_in_requestsAndDurations516); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_requestsAndDurations518); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:86:3: ( '-' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==15) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:86:4: '-'
					{
					match(input,15,FOLLOW_15_in_requestsAndDurations523); 
					}
					break;

				default :
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
			}

			int jobNumber = 1;
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:88:3: ( NEWLINE ( WHITESPACE rr= number )+ )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==NEWLINE) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:89:3: NEWLINE ( WHITESPACE rr= number )+
					{
					match(input,NEWLINE,FOLLOW_NEWLINE_in_requestsAndDurations537); 
					 List<Integer> resources = new ArrayList<Integer>(); 
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:91:5: ( WHITESPACE rr= number )+
					int cnt5=0;
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( (LA5_0==WHITESPACE) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:91:6: WHITESPACE rr= number
							{
							match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestsAndDurations552); 
							pushFollow(FOLLOW_number_in_requestsAndDurations556);
							rr=number();
							state._fsp--;

							resources.add(rr);
							}
							break;

						default :
							if ( cnt5 >= 1 ) break loop5;
							EarlyExitException eee = new EarlyExitException(5, input);
							throw eee;
						}
						cnt5++;
					}


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
					break;

				default :
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "requestsAndDurations"



	// $ANTLR start "resourceAvailabilities"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:107:1: resourceAvailabilities returns [List<Integer> value] : 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+ ;
	public final List<Integer> resourceAvailabilities() throws RecognitionException {
		List<Integer> value = null;


		Integer number7 =null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:107:53: ( 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:108:3: 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+
			{
			value = new ArrayList<Integer>();
			match(input,23,FOLLOW_23_in_resourceAvailabilities590); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_resourceAvailabilities592); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:110:3: ( . )+
			int cnt7=0;
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==NEWLINE) ) {
					alt7=2;
				}
				else if ( ((LA7_0 >= ALPHABET && LA7_0 <= DIGIT)||(LA7_0 >= NUMBER && LA7_0 <= 32)) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:110:3: .
					{
					matchAny(input); 
					}
					break;

				default :
					if ( cnt7 >= 1 ) break loop7;
					EarlyExitException eee = new EarlyExitException(7, input);
					throw eee;
				}
				cnt7++;
			}

			match(input,NEWLINE,FOLLOW_NEWLINE_in_resourceAvailabilities599); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:111:3: ( WHITESPACE number )+
			int cnt8=0;
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==WHITESPACE) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:111:4: WHITESPACE number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_resourceAvailabilities604); 
					pushFollow(FOLLOW_number_in_resourceAvailabilities606);
					number7=number();
					state._fsp--;

					value.add(number7);
					}
					break;

				default :
					if ( cnt8 >= 1 ) break loop8;
					EarlyExitException eee = new EarlyExitException(8, input);
					throw eee;
				}
				cnt8++;
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "resourceAvailabilities"



	// $ANTLR start "number"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:113:1: number returns [Integer value] : NUMBER ;
	public final Integer number() throws RecognitionException {
		Integer value = null;


		Token NUMBER8=null;

		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:113:31: ( NUMBER )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:113:33: NUMBER
			{
			NUMBER8=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_number621); 
			value = toInteger(NUMBER8);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "number"

	// Delegated rules



	public static final BitSet FOLLOW_BEGIN_PAUSE_in_parse43 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_sourceInfo_in_parse45 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse50 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_summary_in_parse52 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse57 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_projects_in_parse59 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse64 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_precedence_in_parse66 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse71 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_requestsAndDurations_in_parse73 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse78 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_resourceAvailabilities_in_parse80 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse89 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_25_in_sourceInfo99 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo101 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_sourceInfo104 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo106 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_STRING_in_sourceInfo108 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_sourceInfo110 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_27_in_sourceInfo114 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_sourceInfo116 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo118 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_sourceInfo120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_31_in_summary133 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary135 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary154 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary156 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary160 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary162 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_summary166 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary174 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary176 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary180 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary182 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_26_in_summary186 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary188 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary208 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary210 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary214 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary216 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_24_in_summary220 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary222 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary226 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_summary228 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary230 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary244 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary246 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary250 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary252 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_21_in_summary254 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary256 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary260 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_summary262 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary264 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary275 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary277 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary281 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary283 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_summary285 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary287 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary291 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_summary293 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary295 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary300 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary302 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary306 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary308 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_summary310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_projects327 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_projects329 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_32_in_projects333 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_projects335 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects340 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects344 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects349 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects353 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects358 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects362 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects367 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects371 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects375 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects379 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects383 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects387 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_19_in_precedence404 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_precedence406 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_precedence410 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_precedenceLine_in_precedence419 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_NEWLINE_in_precedenceLine439 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine444 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine448 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine452 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine456 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine460 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine464 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine474 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine478 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine491 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_22_in_requestsAndDurations510 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_requestsAndDurations512 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_29_in_requestsAndDurations516 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_requestsAndDurations518 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_requestsAndDurations523 = new BitSet(new long[]{0x0000000000008080L});
	public static final BitSet FOLLOW_NEWLINE_in_requestsAndDurations537 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestsAndDurations552 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_requestsAndDurations556 = new BitSet(new long[]{0x0000000000000882L});
	public static final BitSet FOLLOW_23_in_resourceAvailabilities590 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_resourceAvailabilities592 = new BitSet(new long[]{0x00000001FFFFFFF0L});
	public static final BitSet FOLLOW_NEWLINE_in_resourceAvailabilities599 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_resourceAvailabilities604 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_resourceAvailabilities606 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_NUMBER_in_number621 = new BitSet(new long[]{0x0000000000000002L});
}
