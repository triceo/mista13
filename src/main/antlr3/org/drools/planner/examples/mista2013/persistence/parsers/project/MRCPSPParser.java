// $ANTLR 3.5 /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g 2013-01-19 17:36:54

package org.drools.planner.examples.mista2013.persistence.parsers.project;


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


	  // java code here



	// $ANTLR start "parse"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:20:1: parse : BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE ;
	public final void parse() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:20:7: ( BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:20:9: BEGIN_PAUSE sourceInfo PAUSE summary PAUSE projects PAUSE precedence PAUSE requestsAndDurations PAUSE resourceAvailabilities PAUSE
			{
			match(input,BEGIN_PAUSE,FOLLOW_BEGIN_PAUSE_in_parse36); 
			pushFollow(FOLLOW_sourceInfo_in_parse38);
			sourceInfo();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse40); 
			pushFollow(FOLLOW_summary_in_parse42);
			summary();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse44); 
			pushFollow(FOLLOW_projects_in_parse46);
			projects();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse48); 
			pushFollow(FOLLOW_precedence_in_parse50);
			precedence();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse52); 
			pushFollow(FOLLOW_requestsAndDurations_in_parse54);
			requestsAndDurations();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse56); 
			pushFollow(FOLLOW_resourceAvailabilities_in_parse58);
			resourceAvailabilities();
			state._fsp--;

			match(input,PAUSE,FOLLOW_PAUSE_in_parse60); 
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
	// $ANTLR end "parse"



	// $ANTLR start "sourceInfo"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:22:1: sourceInfo : 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number ;
	public final void sourceInfo() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:22:11: ( 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:23:3: 'file with basedata' WHITESPACE ':' WHITESPACE STRING NEWLINE 'initial value random generator' ':' WHITESPACE number
			{
			match(input,25,FOLLOW_25_in_sourceInfo70); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo72); 
			match(input,16,FOLLOW_16_in_sourceInfo75); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo77); 
			match(input,STRING,FOLLOW_STRING_in_sourceInfo79); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_sourceInfo81); 
			match(input,27,FOLLOW_27_in_sourceInfo85); 
			match(input,16,FOLLOW_16_in_sourceInfo87); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_sourceInfo89); 
			pushFollow(FOLLOW_number_in_sourceInfo91);
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
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:26:1: summary : 'projects' WHITESPACE ':' WHITESPACE number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE number WHITESPACE 'D' ;
	public final void summary() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:26:8: ( 'projects' WHITESPACE ':' WHITESPACE number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE number WHITESPACE 'D' )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:27:3: 'projects' WHITESPACE ':' WHITESPACE number NEWLINE 'jobs (incl. supersource/sink )' ':' WHITESPACE number NEWLINE 'horizon' WHITESPACE ':' WHITESPACE number NEWLINE 'RESOURCES' NEWLINE WHITESPACE '- renewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'R' NEWLINE WHITESPACE '- nonrenewable' WHITESPACE ':' WHITESPACE number WHITESPACE 'N' NEWLINE WHITESPACE '- doubly constrained' WHITESPACE ':' WHITESPACE number WHITESPACE 'D'
			{
			match(input,31,FOLLOW_31_in_summary100); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary102); 
			match(input,16,FOLLOW_16_in_summary121); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary123); 
			pushFollow(FOLLOW_number_in_summary125);
			number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary127); 
			match(input,30,FOLLOW_30_in_summary131); 
			match(input,16,FOLLOW_16_in_summary139); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary141); 
			pushFollow(FOLLOW_number_in_summary143);
			number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary145); 
			match(input,26,FOLLOW_26_in_summary149); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary151); 
			match(input,16,FOLLOW_16_in_summary171); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary173); 
			pushFollow(FOLLOW_number_in_summary175);
			number();
			state._fsp--;

			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary177); 
			match(input,24,FOLLOW_24_in_summary181); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary183); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary187); 
			match(input,14,FOLLOW_14_in_summary189); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary191); 
			match(input,16,FOLLOW_16_in_summary205); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary207); 
			pushFollow(FOLLOW_number_in_summary209);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary211); 
			match(input,21,FOLLOW_21_in_summary213); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary215); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary219); 
			match(input,13,FOLLOW_13_in_summary221); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary223); 
			match(input,16,FOLLOW_16_in_summary234); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary236); 
			pushFollow(FOLLOW_number_in_summary238);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary240); 
			match(input,18,FOLLOW_18_in_summary242); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_summary244); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary248); 
			match(input,12,FOLLOW_12_in_summary250); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary252); 
			match(input,16,FOLLOW_16_in_summary257); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary259); 
			pushFollow(FOLLOW_number_in_summary261);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_summary263); 
			match(input,17,FOLLOW_17_in_summary265); 
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
	// $ANTLR end "summary"



	// $ANTLR start "projects"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:35:1: projects : 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number ;
	public final void projects() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:35:9: ( 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:36:3: 'PROJECT INFORMATION:' NEWLINE 'pronr. #jobs rel.date duedate tardcost MPM-Time' NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number WHITESPACE number
			{
			match(input,20,FOLLOW_20_in_projects275); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_projects277); 
			match(input,32,FOLLOW_32_in_projects281); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_projects283); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects288); 
			pushFollow(FOLLOW_number_in_projects290);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects295); 
			pushFollow(FOLLOW_number_in_projects297);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects302); 
			pushFollow(FOLLOW_number_in_projects304);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects309); 
			pushFollow(FOLLOW_number_in_projects311);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects315); 
			pushFollow(FOLLOW_number_in_projects317);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_projects321); 
			pushFollow(FOLLOW_number_in_projects323);
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
	// $ANTLR end "projects"



	// $ANTLR start "precedence"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:45:1: precedence : 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+ ;
	public final void precedence() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:45:11: ( 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:46:3: 'PRECEDENCE RELATIONS:' NEWLINE 'jobnr. #modes #successors successors' ( precedenceLine )+
			{
			match(input,19,FOLLOW_19_in_precedence332); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_precedence334); 
			match(input,28,FOLLOW_28_in_precedence338); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:48:3: ( precedenceLine )+
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
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:48:4: precedenceLine
					{
					pushFollow(FOLLOW_precedenceLine_in_precedence343);
					precedenceLine();
					state._fsp--;

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
	}
	// $ANTLR end "precedence"



	// $ANTLR start "precedenceLine"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:50:1: precedenceLine : NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number ( WHITESPACE number )* ( WHITESPACE )* ;
	public final void precedenceLine() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:50:15: ( NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number ( WHITESPACE number )* ( WHITESPACE )* )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:51:3: NEWLINE WHITESPACE number WHITESPACE number WHITESPACE number ( WHITESPACE number )* ( WHITESPACE )*
			{
			match(input,NEWLINE,FOLLOW_NEWLINE_in_precedenceLine357); 
			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine362); 
			pushFollow(FOLLOW_number_in_precedenceLine364);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine369); 
			pushFollow(FOLLOW_number_in_precedenceLine371);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine376); 
			pushFollow(FOLLOW_number_in_precedenceLine378);
			number();
			state._fsp--;

			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:55:3: ( WHITESPACE number )*
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
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:55:4: WHITESPACE number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine384); 
					pushFollow(FOLLOW_number_in_precedenceLine386);
					number();
					state._fsp--;

					}
					break;

				default :
					break loop2;
				}
			}

			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:56:3: ( WHITESPACE )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==WHITESPACE) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:56:4: WHITESPACE
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_precedenceLine393); 
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
	}
	// $ANTLR end "precedenceLine"



	// $ANTLR start "requestsAndDurations"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:58:1: requestsAndDurations : 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( requestLine )+ ;
	public final void requestsAndDurations() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:58:21: ( 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( requestLine )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:59:3: 'REQUESTS/DURATIONS:' NEWLINE 'jobnr. mode duration R 1 R 2 N 1 N 2' NEWLINE ( '-' )+ ( requestLine )+
			{
			match(input,22,FOLLOW_22_in_requestsAndDurations404); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_requestsAndDurations406); 
			match(input,29,FOLLOW_29_in_requestsAndDurations410); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_requestsAndDurations412); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:61:3: ( '-' )+
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
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:61:4: '-'
					{
					match(input,15,FOLLOW_15_in_requestsAndDurations417); 
					}
					break;

				default :
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
			}

			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:62:3: ( requestLine )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==NEWLINE) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:62:4: requestLine
					{
					pushFollow(FOLLOW_requestLine_in_requestsAndDurations424);
					requestLine();
					state._fsp--;

					}
					break;

				default :
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
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
	}
	// $ANTLR end "requestsAndDurations"



	// $ANTLR start "requestLine"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:64:1: requestLine : NEWLINE ( WHITESPACE number | WHITESPACE ) WHITESPACE number WHITESPACE number ( WHITESPACE number )+ ;
	public final void requestLine() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:64:12: ( NEWLINE ( WHITESPACE number | WHITESPACE ) WHITESPACE number WHITESPACE number ( WHITESPACE number )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:65:3: NEWLINE ( WHITESPACE number | WHITESPACE ) WHITESPACE number WHITESPACE number ( WHITESPACE number )+
			{
			match(input,NEWLINE,FOLLOW_NEWLINE_in_requestLine437); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:66:3: ( WHITESPACE number | WHITESPACE )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==WHITESPACE) ) {
				int LA6_1 = input.LA(2);
				if ( (LA6_1==NUMBER) ) {
					alt6=1;
				}
				else if ( (LA6_1==WHITESPACE) ) {
					alt6=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 6, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:66:4: WHITESPACE number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestLine443); 
					pushFollow(FOLLOW_number_in_requestLine445);
					number();
					state._fsp--;

					}
					break;
				case 2 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:66:24: WHITESPACE
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestLine449); 
					}
					break;

			}

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestLine455); 
			pushFollow(FOLLOW_number_in_requestLine457);
			number();
			state._fsp--;

			match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestLine462); 
			pushFollow(FOLLOW_number_in_requestLine464);
			number();
			state._fsp--;

			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:69:3: ( WHITESPACE number )+
			int cnt7=0;
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==WHITESPACE) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:69:4: WHITESPACE number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_requestLine470); 
					pushFollow(FOLLOW_number_in_requestLine472);
					number();
					state._fsp--;

					}
					break;

				default :
					if ( cnt7 >= 1 ) break loop7;
					EarlyExitException eee = new EarlyExitException(7, input);
					throw eee;
				}
				cnt7++;
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
	}
	// $ANTLR end "requestLine"



	// $ANTLR start "resourceAvailabilities"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:71:1: resourceAvailabilities : 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+ ;
	public final void resourceAvailabilities() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:71:23: ( 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+ )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:72:3: 'RESOURCEAVAILABILITIES:' NEWLINE ( . )+ NEWLINE ( WHITESPACE number )+
			{
			match(input,23,FOLLOW_23_in_resourceAvailabilities485); 
			match(input,NEWLINE,FOLLOW_NEWLINE_in_resourceAvailabilities487); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:73:3: ( . )+
			int cnt8=0;
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==NEWLINE) ) {
					alt8=2;
				}
				else if ( ((LA8_0 >= ALPHABET && LA8_0 <= DIGIT)||(LA8_0 >= NUMBER && LA8_0 <= 32)) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:73:3: .
					{
					matchAny(input); 
					}
					break;

				default :
					if ( cnt8 >= 1 ) break loop8;
					EarlyExitException eee = new EarlyExitException(8, input);
					throw eee;
				}
				cnt8++;
			}

			match(input,NEWLINE,FOLLOW_NEWLINE_in_resourceAvailabilities494); 
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:74:3: ( WHITESPACE number )+
			int cnt9=0;
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==WHITESPACE) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:74:4: WHITESPACE number
					{
					match(input,WHITESPACE,FOLLOW_WHITESPACE_in_resourceAvailabilities499); 
					pushFollow(FOLLOW_number_in_resourceAvailabilities501);
					number();
					state._fsp--;

					}
					break;

				default :
					if ( cnt9 >= 1 ) break loop9;
					EarlyExitException eee = new EarlyExitException(9, input);
					throw eee;
				}
				cnt9++;
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
	}
	// $ANTLR end "resourceAvailabilities"



	// $ANTLR start "number"
	// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:76:1: number : NUMBER ;
	public final void number() throws RecognitionException {
		try {
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:76:7: ( NUMBER )
			// /home/lpetrovi/Workspaces/Stuff/mista2013/src/main/antlr3/org/drools/planner/examples/mista2013/persistence/parsers/project/MRCPSP.g:76:9: NUMBER
			{
			match(input,NUMBER,FOLLOW_NUMBER_in_number510); 
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
	// $ANTLR end "number"

	// Delegated rules



	public static final BitSet FOLLOW_BEGIN_PAUSE_in_parse36 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_sourceInfo_in_parse38 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse40 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_summary_in_parse42 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse44 = new BitSet(new long[]{0x0000000000100000L});
	public static final BitSet FOLLOW_projects_in_parse46 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse48 = new BitSet(new long[]{0x0000000000080000L});
	public static final BitSet FOLLOW_precedence_in_parse50 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse52 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_requestsAndDurations_in_parse54 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse56 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_resourceAvailabilities_in_parse58 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_PAUSE_in_parse60 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_25_in_sourceInfo70 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo72 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_sourceInfo75 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo77 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_STRING_in_sourceInfo79 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_sourceInfo81 = new BitSet(new long[]{0x0000000008000000L});
	public static final BitSet FOLLOW_27_in_sourceInfo85 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_sourceInfo87 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_sourceInfo89 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_sourceInfo91 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_31_in_summary100 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary102 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary121 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary123 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary125 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary127 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_30_in_summary131 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary139 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary141 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary143 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary145 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_26_in_summary149 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary151 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary171 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary173 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary175 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary177 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_24_in_summary181 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary183 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary187 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_14_in_summary189 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary191 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary205 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary207 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary209 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary211 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_21_in_summary213 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary215 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary219 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_summary221 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary223 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary234 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary236 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary238 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary240 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_18_in_summary242 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_summary244 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary248 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_12_in_summary250 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary252 = new BitSet(new long[]{0x0000000000010000L});
	public static final BitSet FOLLOW_16_in_summary257 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary259 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_summary261 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_summary263 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_summary265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_20_in_projects275 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_projects277 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_32_in_projects281 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_projects283 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects288 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects290 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects295 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects297 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects302 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects304 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects309 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects311 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects315 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects317 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_projects321 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_projects323 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_19_in_precedence332 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_precedence334 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_28_in_precedence338 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_precedenceLine_in_precedence343 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_NEWLINE_in_precedenceLine357 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine362 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine364 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine369 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine371 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine376 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine378 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine384 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_precedenceLine386 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_WHITESPACE_in_precedenceLine393 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_22_in_requestsAndDurations404 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_requestsAndDurations406 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_29_in_requestsAndDurations410 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_requestsAndDurations412 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_15_in_requestsAndDurations417 = new BitSet(new long[]{0x0000000000008080L});
	public static final BitSet FOLLOW_requestLine_in_requestsAndDurations424 = new BitSet(new long[]{0x0000000000000082L});
	public static final BitSet FOLLOW_NEWLINE_in_requestLine437 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestLine443 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_requestLine445 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestLine449 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestLine455 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_requestLine457 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestLine462 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_requestLine464 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_requestLine470 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_requestLine472 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_23_in_resourceAvailabilities485 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_NEWLINE_in_resourceAvailabilities487 = new BitSet(new long[]{0x00000001FFFFFFF0L});
	public static final BitSet FOLLOW_NEWLINE_in_resourceAvailabilities494 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_WHITESPACE_in_resourceAvailabilities499 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_number_in_resourceAvailabilities501 = new BitSet(new long[]{0x0000000000000802L});
	public static final BitSet FOLLOW_NUMBER_in_number510 = new BitSet(new long[]{0x0000000000000002L});
}
