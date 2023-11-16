package com.yigongil.backend.domain.round;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.ui.doc.MustDoApi;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rounds/{roundId}")
@RestController
public class MustDoController implements MustDoApi {

    private final MustDoService mustDoService;

    public MustDoController(MustDoService mustDoService) {
        this.mustDoService = mustDoService;
    }

    @PutMapping("/todos")
    public ResponseEntity<Void> updateMustDo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid MustDoUpdateRequest request
    ) {
        mustDoService.updateMustDo(member, roundId, request);
        return ResponseEntity.ok().build();
    }
}
