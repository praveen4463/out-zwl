// Generated from com/zylitics/zwl/antlr4/ZwlLexer.g4 by ANTLR 4.8
package com.zylitics.zwl.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ZwlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, ELSE=2, FOR=3, WHILE=4, IN=5, TO=6, LBRACE=7, RBRACE=8, LPAREN=9, 
		RPAREN=10, LBRAC=11, RBRAC=12, COMMA=13, DOT=14, QUOTE=15, BACKTICK=16, 
		ASSIGN=17, GT=18, LT=19, BANG=20, DOUBLE_QUESTION=21, EQUAL=22, LE=23, 
		GE=24, NOTEQUAL=25, AND=26, OR=27, INC=28, DEC=29, ADD=30, SUB=31, MUL=32, 
		DIV=33, MOD=34, NumberLiteral=35, BooleanLiteral=36, RawStringLiteral=37, 
		StringLiteral=38, Identifier=39, WS=40, COMMENT=41;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "ELSE", "FOR", "WHILE", "IN", "TO", "LBRACE", "RBRACE", "LPAREN", 
			"RPAREN", "LBRAC", "RBRAC", "COMMA", "DOT", "QUOTE", "BACKTICK", "ASSIGN", 
			"GT", "LT", "BANG", "DOUBLE_QUESTION", "EQUAL", "LE", "GE", "NOTEQUAL", 
			"AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "MOD", "NumberLiteral", 
			"Integer", "DIGIT", "BooleanLiteral", "RawStringLiteral", "StringLiteral", 
			"StringCharacters", "HexDigit", "StringCharacter", "Identifier", "WS", 
			"COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'else'", "'for'", "'while'", "'in'", "'to'", "'{'", "'}'", 
			"'('", "')'", "'['", "']'", "','", "'.'", "'\"'", "'`'", "'='", "'>'", 
			"'<'", "'!'", "'??'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", 
			"'++'", "'--'", "'+'", "'-'", "'*'", "'/'", "'%'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "ELSE", "FOR", "WHILE", "IN", "TO", "LBRACE", "RBRACE", "LPAREN", 
			"RPAREN", "LBRAC", "RBRAC", "COMMA", "DOT", "QUOTE", "BACKTICK", "ASSIGN", 
			"GT", "LT", "BANG", "DOUBLE_QUESTION", "EQUAL", "LE", "GE", "NOTEQUAL", 
			"AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "MOD", "NumberLiteral", 
			"BooleanLiteral", "RawStringLiteral", "StringLiteral", "Identifier", 
			"WS", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ZwlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ZwlLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2+\u011f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3"+
		"\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3"+
		"\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3"+
		"\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#"+
		"\3$\3$\3$\7$\u00bc\n$\f$\16$\u00bf\13$\5$\u00c1\n$\3%\3%\7%\u00c5\n%\f"+
		"%\16%\u00c8\13%\3%\5%\u00cb\n%\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3"+
		"\'\5\'\u00d8\n\'\3(\3(\7(\u00dc\n(\f(\16(\u00df\13(\3(\3(\3)\3)\5)\u00e5"+
		"\n)\3)\3)\3*\6*\u00ea\n*\r*\16*\u00eb\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3"+
		",\3,\5,\u00fa\n,\3-\3-\7-\u00fe\n-\f-\16-\u0101\13-\3.\6.\u0104\n.\r."+
		"\16.\u0105\3.\3.\3/\3/\7/\u010c\n/\f/\16/\u010f\13/\3/\3/\3/\3/\7/\u0115"+
		"\n/\f/\16/\u0118\13/\3/\3/\5/\u011c\n/\3/\3/\5\u00dd\u00e4\u0116\2\60"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"+
		"= ?!A\"C#E$G%I\2K\2M&O\'Q(S\2U\2W\2Y)[*]+\3\2\13\3\2\63;\3\2\62;\5\2\62"+
		";CHch\6\2\f\f\17\17$$^^\7\2$$^^ppttvv\5\2C\\aac|\6\2\62;C\\aac|\5\2\13"+
		"\f\16\17\"\"\4\2\f\f\17\17\2\u0128\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\3_\3\2\2\2\5b\3\2\2\2\7g\3\2\2\2\tk\3"+
		"\2\2\2\13q\3\2\2\2\rt\3\2\2\2\17w\3\2\2\2\21y\3\2\2\2\23{\3\2\2\2\25}"+
		"\3\2\2\2\27\177\3\2\2\2\31\u0081\3\2\2\2\33\u0083\3\2\2\2\35\u0085\3\2"+
		"\2\2\37\u0087\3\2\2\2!\u0089\3\2\2\2#\u008b\3\2\2\2%\u008d\3\2\2\2\'\u008f"+
		"\3\2\2\2)\u0091\3\2\2\2+\u0093\3\2\2\2-\u0096\3\2\2\2/\u0099\3\2\2\2\61"+
		"\u009c\3\2\2\2\63\u009f\3\2\2\2\65\u00a2\3\2\2\2\67\u00a5\3\2\2\29\u00a8"+
		"\3\2\2\2;\u00ab\3\2\2\2=\u00ae\3\2\2\2?\u00b0\3\2\2\2A\u00b2\3\2\2\2C"+
		"\u00b4\3\2\2\2E\u00b6\3\2\2\2G\u00b8\3\2\2\2I\u00ca\3\2\2\2K\u00cc\3\2"+
		"\2\2M\u00d7\3\2\2\2O\u00d9\3\2\2\2Q\u00e2\3\2\2\2S\u00e9\3\2\2\2U\u00ed"+
		"\3\2\2\2W\u00f9\3\2\2\2Y\u00fb\3\2\2\2[\u0103\3\2\2\2]\u011b\3\2\2\2_"+
		"`\7k\2\2`a\7h\2\2a\4\3\2\2\2bc\7g\2\2cd\7n\2\2de\7u\2\2ef\7g\2\2f\6\3"+
		"\2\2\2gh\7h\2\2hi\7q\2\2ij\7t\2\2j\b\3\2\2\2kl\7y\2\2lm\7j\2\2mn\7k\2"+
		"\2no\7n\2\2op\7g\2\2p\n\3\2\2\2qr\7k\2\2rs\7p\2\2s\f\3\2\2\2tu\7v\2\2"+
		"uv\7q\2\2v\16\3\2\2\2wx\7}\2\2x\20\3\2\2\2yz\7\177\2\2z\22\3\2\2\2{|\7"+
		"*\2\2|\24\3\2\2\2}~\7+\2\2~\26\3\2\2\2\177\u0080\7]\2\2\u0080\30\3\2\2"+
		"\2\u0081\u0082\7_\2\2\u0082\32\3\2\2\2\u0083\u0084\7.\2\2\u0084\34\3\2"+
		"\2\2\u0085\u0086\7\60\2\2\u0086\36\3\2\2\2\u0087\u0088\7$\2\2\u0088 \3"+
		"\2\2\2\u0089\u008a\7b\2\2\u008a\"\3\2\2\2\u008b\u008c\7?\2\2\u008c$\3"+
		"\2\2\2\u008d\u008e\7@\2\2\u008e&\3\2\2\2\u008f\u0090\7>\2\2\u0090(\3\2"+
		"\2\2\u0091\u0092\7#\2\2\u0092*\3\2\2\2\u0093\u0094\7A\2\2\u0094\u0095"+
		"\7A\2\2\u0095,\3\2\2\2\u0096\u0097\7?\2\2\u0097\u0098\7?\2\2\u0098.\3"+
		"\2\2\2\u0099\u009a\7>\2\2\u009a\u009b\7?\2\2\u009b\60\3\2\2\2\u009c\u009d"+
		"\7@\2\2\u009d\u009e\7?\2\2\u009e\62\3\2\2\2\u009f\u00a0\7#\2\2\u00a0\u00a1"+
		"\7?\2\2\u00a1\64\3\2\2\2\u00a2\u00a3\7(\2\2\u00a3\u00a4\7(\2\2\u00a4\66"+
		"\3\2\2\2\u00a5\u00a6\7~\2\2\u00a6\u00a7\7~\2\2\u00a78\3\2\2\2\u00a8\u00a9"+
		"\7-\2\2\u00a9\u00aa\7-\2\2\u00aa:\3\2\2\2\u00ab\u00ac\7/\2\2\u00ac\u00ad"+
		"\7/\2\2\u00ad<\3\2\2\2\u00ae\u00af\7-\2\2\u00af>\3\2\2\2\u00b0\u00b1\7"+
		"/\2\2\u00b1@\3\2\2\2\u00b2\u00b3\7,\2\2\u00b3B\3\2\2\2\u00b4\u00b5\7\61"+
		"\2\2\u00b5D\3\2\2\2\u00b6\u00b7\7\'\2\2\u00b7F\3\2\2\2\u00b8\u00c0\5I"+
		"%\2\u00b9\u00bd\5\35\17\2\u00ba\u00bc\5K&\2\u00bb\u00ba\3\2\2\2\u00bc"+
		"\u00bf\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c1\3\2"+
		"\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00b9\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1"+
		"H\3\2\2\2\u00c2\u00c6\t\2\2\2\u00c3\u00c5\5K&\2\u00c4\u00c3\3\2\2\2\u00c5"+
		"\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00cb\3\2"+
		"\2\2\u00c8\u00c6\3\2\2\2\u00c9\u00cb\7\62\2\2\u00ca\u00c2\3\2\2\2\u00ca"+
		"\u00c9\3\2\2\2\u00cbJ\3\2\2\2\u00cc\u00cd\t\3\2\2\u00cdL\3\2\2\2\u00ce"+
		"\u00cf\7v\2\2\u00cf\u00d0\7t\2\2\u00d0\u00d1\7w\2\2\u00d1\u00d8\7g\2\2"+
		"\u00d2\u00d3\7h\2\2\u00d3\u00d4\7c\2\2\u00d4\u00d5\7n\2\2\u00d5\u00d6"+
		"\7u\2\2\u00d6\u00d8\7g\2\2\u00d7\u00ce\3\2\2\2\u00d7\u00d2\3\2\2\2\u00d8"+
		"N\3\2\2\2\u00d9\u00dd\5!\21\2\u00da\u00dc\13\2\2\2\u00db\u00da\3\2\2\2"+
		"\u00dc\u00df\3\2\2\2\u00dd\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00e0"+
		"\3\2\2\2\u00df\u00dd\3\2\2\2\u00e0\u00e1\5!\21\2\u00e1P\3\2\2\2\u00e2"+
		"\u00e4\5\37\20\2\u00e3\u00e5\5S*\2\u00e4\u00e5\3\2\2\2\u00e4\u00e3\3\2"+
		"\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e7\5\37\20\2\u00e7R\3\2\2\2\u00e8\u00ea"+
		"\5W,\2\u00e9\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb"+
		"\u00ec\3\2\2\2\u00ecT\3\2\2\2\u00ed\u00ee\t\4\2\2\u00eeV\3\2\2\2\u00ef"+
		"\u00fa\n\5\2\2\u00f0\u00f1\7^\2\2\u00f1\u00fa\t\6\2\2\u00f2\u00f3\7^\2"+
		"\2\u00f3\u00f4\7w\2\2\u00f4\u00f5\5U+\2\u00f5\u00f6\5U+\2\u00f6\u00f7"+
		"\5U+\2\u00f7\u00f8\5U+\2\u00f8\u00fa\3\2\2\2\u00f9\u00ef\3\2\2\2\u00f9"+
		"\u00f0\3\2\2\2\u00f9\u00f2\3\2\2\2\u00faX\3\2\2\2\u00fb\u00ff\t\7\2\2"+
		"\u00fc\u00fe\t\b\2\2\u00fd\u00fc\3\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd"+
		"\3\2\2\2\u00ff\u0100\3\2\2\2\u0100Z\3\2\2\2\u0101\u00ff\3\2\2\2\u0102"+
		"\u0104\t\t\2\2\u0103\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0103\3\2"+
		"\2\2\u0105\u0106\3\2\2\2\u0106\u0107\3\2\2\2\u0107\u0108\b.\2\2\u0108"+
		"\\\3\2\2\2\u0109\u010d\7%\2\2\u010a\u010c\n\n\2\2\u010b\u010a\3\2\2\2"+
		"\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u011c"+
		"\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0111\7\61\2\2\u0111\u0112\7,\2\2\u0112"+
		"\u0116\3\2\2\2\u0113\u0115\13\2\2\2\u0114\u0113\3\2\2\2\u0115\u0118\3"+
		"\2\2\2\u0116\u0117\3\2\2\2\u0116\u0114\3\2\2\2\u0117\u0119\3\2\2\2\u0118"+
		"\u0116\3\2\2\2\u0119\u011a\7,\2\2\u011a\u011c\7\61\2\2\u011b\u0109\3\2"+
		"\2\2\u011b\u0110\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u011e\b/\2\2\u011e"+
		"^\3\2\2\2\21\2\u00bd\u00c0\u00c6\u00ca\u00d7\u00dd\u00e4\u00eb\u00f9\u00ff"+
		"\u0105\u010d\u0116\u011b\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}