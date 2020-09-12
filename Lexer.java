package compiler;
//TODO: incorporate signed numbers into the lexer
import java.util.*;
import java.io.*;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

public class Lexer {

    public String inputString;
    public List<Token> tokenList;

    private enum LexerStatus {
        StartStatus, IntStatus, DecimalStatus, DFStatus, CStatus, OStatus, SStatus,
    }

    public Lexer(String inputString) {
        this.inputString = inputString;
        tokenList = this.tokenize();
    }

    public List<Token> tokenize() {

        List<Token> tokenList = new ArrayList<Token>();
        StringBuilder wholeSDN = new StringBuilder();
        StringBuilder cosString = new StringBuilder();

        LexerStatus lexerStatus = LexerStatus.StartStatus;

        Scanner sc = new Scanner(System.in);
        String inputString = sc.next();
        char[] charArray = inputString.toCharArray();

        for (char c : charArray) {
            if ((lexerStatus == LexerStatus.IntStatus || lexerStatus == LexerStatus.DFStatus) && !isDigit(c) && c != '.') {
                if (wholeSDN.length() > 0) {
                    double val = (double) c;
                    wholeSDN = null;
                    tokenList.add(new Token(Token.Tokens.SDNToken, val));
                }
            } else if (isWhitespace(c)) {
                if (c == '\n') {
                    tokenList.add(new Token(Token.Tokens.LineEndToken));
                }
            } else if (c == '+') {
                tokenList.add(new Token(Token.Tokens.AddToken));
            } else if (c == '-') {
                tokenList.add(new Token(Token.Tokens.SubToken));
            } else if (c == '*') {
                tokenList.add(new Token(Token.Tokens.MultToken));
            } else if (c == '!') {
                tokenList.add(new Token(Token.Tokens.FactToken));
            } else if (c == 'c') {
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
                }
                wholeSDN.append(c);
            } else if (c == '.') {
                if (lexerStatus == LexerStatus.IntStatus) {
                    lexerStatus = LexerStatus.DecimalStatus;
                }
                wholeSDN.append(c);
            }
            tokenList.add(new Token(Token.Tokens.LineEndToken));
        }
        this.tokenList = tokenList;
        return this.tokenList;
    }

    public Token scan() {

        StringBuilder wholeSDN = new StringBuilder();
        StringBuilder cosString = new StringBuilder();

        LexerStatus lexerStatus = LexerStatus.StartStatus;

        Scanner sc = new Scanner(System.in);
        String inputString = sc.next();
        char[] charArray = inputString.toCharArray();

        for (char c : charArray) {
            if ((lexerStatus == LexerStatus.IntStatus || lexerStatus == LexerStatus.DFStatus) && !isDigit(c) && c != '.') {
                if (wholeSDN.length() > 0) {
                    double val = (double) c;
                    wholeSDN = null;
                    return (new Token(Token.Tokens.SDNToken, val));
                }
            } else if (isWhitespace(c)) {
                if (c == '\n') {
                    return (new Token(Token.Tokens.LineEndToken));
                }
            } else if (c == '+') {
                return (new Token(Token.Tokens.AddToken));
            } else if (c == '-') {
                return (new Token(Token.Tokens.SubToken));
            } else if (c == '*') {
                return (new Token(Token.Tokens.MultToken));
            } else if (c == '!') {
                return (new Token(Token.Tokens.FactToken));
            } else if (c == 'c') {
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
                    cosString = null;
                    return (new Token(Token.Tokens.CosineToken));
                }
            }
            else if (isDigit(c)) {
                if (lexerStatus == LexerStatus.StartStatus) {
                    lexerStatus = LexerStatus.IntStatus;
                } else if (lexerStatus == LexerStatus.DecimalStatus) {
                    lexerStatus = LexerStatus.DFStatus;
                }
                wholeSDN.append(c);
            } else if (c == '.') {
                if (lexerStatus == LexerStatus.IntStatus) {
                    lexerStatus = LexerStatus.DecimalStatus;
                }
                wholeSDN.append(c);
            }
            else {
                return (new Token(Token.Tokens.InvalidToken));
            }
        }
        return (new Token(Token.Tokens.LineEndToken));
    }

}
