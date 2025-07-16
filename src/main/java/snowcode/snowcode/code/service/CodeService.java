package snowcode.snowcode.code.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowcode.snowcode.code.domain.Code;
import snowcode.snowcode.code.domain.Language;
import snowcode.snowcode.code.dto.CodeRequest;
import snowcode.snowcode.code.dto.CodeResponse;
import snowcode.snowcode.code.repository.CodeRepository;
import snowcode.snowcode.submission.domain.Submission;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    @Transactional
    public CodeResponse createCode(Submission submission, CodeRequest dto) {
        Code code = Code.createCode(dto.code(), true, Language.of(dto.language()), submission);
        codeRepository.save(code);
        return CodeResponse.from(code);
    }
}
