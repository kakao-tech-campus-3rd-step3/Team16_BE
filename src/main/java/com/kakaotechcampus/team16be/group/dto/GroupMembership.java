package com.kakaotechcampus.team16be.group.dto;

import java.util.List;

public record GroupMembership(
        List<String> leaderOf,
        List<String> memberOf
) {
}
