package com.SeedOasis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class TestLikeId implements Serializable {

    private String member;
    private Long test;

/*    @Override
    public int hashCode() {
        return Objects.hash(member, test);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestLikeId other = (TestLikeId) obj;
        return member == other.member && test == other.test;
    }*/
}
