/*
 *    Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.infuzion.web.server.parser;

public enum TokenType {
    KEYWORD_IF,            // if
    KEYWORD_BEGIN,
    KEYWORD_END,
    KEYWORD_ELSE,          // else
    KEYWORD_VAR,           // var
    KEYWORD_ECHO,          // echo
    VAR_NAME,              // any alphabetic word (other than other keywords)
    ASSIGN,                // =
    SEMI,                  // ;
    KEYWORD_TRUE,          // true
    KEYWORD_FALSE,         // false
    BRACKET_LEFT,          // {
    BRACKET_RIGHT,         // }
    TYPE_NUMBER,
    STRING_CONCATENATE,    // .
    OP_PLUS,               // +
    OP_MINUS,              // -
    OP_MULTIPLY,           // *
    OP_DIVIDE,             // /
    OP_LT,                 // <
    OP_LTE,                // <=
    OP_GT,                 // >
    OP_GTE,                // >=
    OP_NOT,                // !
    OP_NOT_EQUAL,          // !=
    OP_EQUALS,             // ==
    OP_EXPONENT,           // ^
    PARENTHESIS_LEFT,      // (
    PARENTHESIS_RIGHT,     // )
    EOF,
}

// Line Comment:          #
// Block Comment:         /* ... */
