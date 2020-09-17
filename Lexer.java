package compiler;
import java.util.*;

import static java.lang.Character.*;

public class Lexer {

    public String inputString;
    public List<Token> tokenList;

    private enum LexerStatus {
        StartStatus, IntStatus, DecimalStatus, DFStatus, CStatus, OStatus, SStatus, AddMinusOpStatus,
    }

    public Lexer(String inputString) throws LexingException {
        this.inputString = inputString;
        tokenList = tokenize(this.inputString);
    }

    public List<Token> tokenize(String inputString) throws LexingException{

        List<Token> tokenList = new ArrayList<Token>();
        StringBuilder wholeSDN = new StringBuilder();
        StringBuilder cosString = new StringBuilder();

        LexerStatus lexerStatus = LexerStatus.StartStatus;

        char[] charArray = inputString.toCharArray();
        for (char c : charArray) {
            if ((lexerStatus == LexerStatus.IntStatus || lexerStatus == LexerStatus.DFStatus) && !isDigit(c) && c != '.') {
                if (wholeSDN.length() > 0) {
                    Double valueToAdd = Double.valueOf(wholeSDN.toString());

                    wholeSDN.setLength(0);

                    tokenList.add(new Token(Token.Tokens.SDNToken, valueToAdd));
                }
            }
            if (isWhitespace(c)) {
                if (c == '\n') {
                    tokenList.add(new Token(Token.Tokens.LineEndToken));
                }
            }
            if (lexerStatus == LexerStatus.AddMinusOpStatus && (c=='+' || c=='-')){
                wholeSDN.append(c);
                lexerStatus = LexerStatus.IntStatus;
            }
            else if (c == '+') {
                if(lexerStatus == LexerStatus.StartStatus){
                    wholeSDN.append(c);
                    lexerStatus = LexerStatus.IntStatus;
                }
                else{
                    lexerStatus = LexerStatus.AddMinusOpStatus;
                    tokenList.add(new Token(Token.Tokens.AddToken));
                }
            }
            else if (c == '-') {
                if(lexerStatus == LexerStatus.StartStatus){
                    wholeSDN.append(c);
                    lexerStatus = LexerStatus.IntStatus;
                }
                else{
                    lexerStatus = LexerStatus.AddMinusOpStatus;
                    tokenList.add(new Token(Token.Tokens.SubToken));
                }
            }
            else if (c == '*') {
                tokenList.add(new Token(Token.Tokens.MultToken));
            }
            else if (c == '!') {
                tokenList.add(new Token(Token.Tokens.FactToken));
            }
            else if (c == 'c') {
                if(cosString.length() == 0){
                    lexerStatus = LexerStatus.CStatus;
                    cosString.append(c);
                }
            }
            else if (c == 'o') {
                if((cosString.length() > 0) && lexerStatus==LexerStatus.CStatus){
                    lexerStatus = LexerStatus.OStatus;
                    cosString.append(c);
                }
            }
            else if (c == 's') {
                if((cosString.length() > 0) && lexerStatus==LexerStatus.OStatus){
                    lexerStatus = LexerStatus.SStatus;
                    cosString.append(c);
                    tokenList.add(new Token(Token.Tokens.CosineToken));
                    cosString = null;
                }
            }
            else if (isDigit(c)) {
                if (lexerStatus == LexerStatus.StartStatus) {
                    lexerStatus = LexerStatus.IntStatus;
                } else if (lexerStatus == LexerStatus.DecimalStatus) {
                    lexerStatus = LexerStatus.DFStatus;
                } else if (lexerStatus == LexerStatus.SStatus) {
                    lexerStatus = LexerStatus.IntStatus;
                } else if (lexerStatus == LexerStatus.AddMinusOpStatus){
                    lexerStatus = LexerStatus.IntStatus;
                }
                wholeSDN.append(c);
            }
            else if (c == '.') {
                if (lexerStatus == LexerStatus.IntStatus) {
                    lexerStatus = LexerStatus.DecimalStatus;
                }
                wholeSDN.append(c);
            }
            else{
                throw new LexingException("Invalid token!");
            }
        }
        if(wholeSDN.length() > 0){
            Double valueToAdd = Double.valueOf(wholeSDN.toString());
            tokenList.add(new Token(Token.Tokens.SDNToken, valueToAdd));
        }
        tokenList.add(new Token(Token.Tokens.LineEndToken));
        this.tokenList = tokenList;
        return this.tokenList;
    }
}