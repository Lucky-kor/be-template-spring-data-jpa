package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.dto.MemberPostDto;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    //    postDto를 Member entity로  받았을 때 멤버객체에 넣어둔 이메일이 DB에 있는지 없는지 검증하는 메서드를 실행하고
//    있다면 예외를, 없다면 Repository.save(Member)
    public Member createMember(Member member) {
//       이메일이 있는지 없는지 확인 있다면 예외처리까지 하고있으니 실행된다면 중복이 없다는 뜻
        verifyExistEmail(member.getEmail());

        return memberRepository.save(member);

//        컨트롤러에서 매핑을 통해 ResponseDto로 변환하여 API 계층에 전달 해줘야하므로 올라가자
    }

    public Member updateMember(Member member) {
        // TODO should business logic
//        멤버가 있는지 없는지 확인을 해야한다
        Member findmember = findVerifiedFindMember(member.getMemberId());
//        수정할 수 있는 값은 이름과 폰 두개이다.
//        다음계층으로 보내줘야하는데 필드값에 널이 들어올 수 있으므로(필드에 전체를 바꿀 필요가 없으니)
//        Optional 클래스로 사용
//       널값이 들어올수 있는 member에 이름을 가져와서 이름이 존재한다면 그 이름을 요청받은 이름의 이름으로 변경한다.
        Optional.ofNullable(member.getName()).ifPresent(name -> findmember.setName(name));
//        마찬가지로 수정가능한 전화번호를 똑같이 생성
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findmember.setPhone(phone));
//        상태를 수정해주는 로직 추가.
        Optional.ofNullable(findmember.getMemberStatus())
                .ifPresent(memberStatus -> findmember.setMemberStatus(memberStatus));
//        그리고나서 DB에저장하겠다 Repository메서드호출

        return memberRepository.save(findmember);
    }

    public Member findMember(long memberId) {
//        DB에서 찾아서 client에게 보내주는 DTO가 될 정보.
        return findVerifiedFindMember(memberId);

    }

    public Page<Member> findMembers(int page, int size) {
        // TODO should business logic
//        찾은 멤버들을 페이지네이션을 적용해서 받아야함
//        DB에서 찾아서 ENTITY > RESPONSEDTO가 되어야함 -> PageResponse 를 담아서 보낼거임
//        처음엔 생성자로 호출해봤지만 PageRequest안에 of - 정적메서드로 구현되어있어서 코드를 수정 했음.
//        PageRequest pageRequest = new PageRequest(page-1,size, Sort.Direction.DESC,"memberId");
//          보낼 때 DB에서 꺼내올 땐  인덱스가 0이므로 -1을 해줬음. 자동으로 내림차순 정렬로 최신화
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "memberId");
//        DB에서 찾은 멤버에 page대한 요청을 넣어서 반환 -> 컨트롤러에서 받아서 DTO로 만들어주고 반환
        return memberRepository.findAll(pageRequest);
    }

    public void deleteMember(long memberId) {
        // TODO should business logic

        throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    //    memberId로 가입이 되어있는지 안되어있는지 확인
    public Member findVerifiedFindMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    //   POST요청을 받았을 때 DB에서 Email이 있는지 검증하는 메서드
    public void verifyExistEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }

}



