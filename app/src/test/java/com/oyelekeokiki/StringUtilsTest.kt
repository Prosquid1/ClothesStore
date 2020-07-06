package com.oyelekeokiki

import com.oyelekeokiki.utils.*
import org.junit.Test

class StringUtilsTest {
    @Test
    fun `format Price`() {
        assert("3".formatPrice() == "£3")
    }

    @Test
    fun `format Price Fail`() {
        assert("0".formatPrice() != "0")
    }

    @Test
    fun `convert to N Characters`() {
        assert("CharacterToBeConverted".convertToNCharacters(5) == "Chara")
    }

    @Test
    fun `convert to N Characters Fail`() {
        assert("CharacterToBeConverted".convertToNCharacters(1) != "0")
    }

    @Test
    fun `convert null to N Characters Fail`() {
        assert(null?.convertToNCharacters(5) == null)
    }
}