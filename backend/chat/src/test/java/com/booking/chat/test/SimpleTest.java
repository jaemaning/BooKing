package com.booking.chat.test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class SimpleTest {

    @Test
    void t1() {

        int a = 1 + 1;

        assertThat(a).isEqualTo(2);
    }

}
