# 코프링-로또

> 이 프로젝트는 제이슨의 코프링 아는척해보기 강의에서 제공된 자료를 기반으로 하며, <br> 프로젝트의 방향성과 구현 내용은 개인적인 목표와 아이디어에 맞추어 진행합니다.

## 프로젝트 개요 ( 초안 )

[로또는 왜 현금으로만 구매하나요?](https://economist.co.kr/article/view/ecn202409110019)

위 기사에 따르면:

- 사행성 조장 방지를 위해 신용카드 결제가 금지되어 있습니다.
- 체크카드는 신용카드와 단말기 구분이 불가능하여 결제가 불가능합니다.

이에 따라 로또 결제 방식의 제약을 해결을 하는 프로젝트입니다.

## 목표

- 엔티티 설계: 로또, 결제 도메인 설계
- 결제 방식 확장: 토스페이 및 카드결제를 통한 온라인 간편 결제
- 지속 성장 가능한 소프트웨어: 유지보수성과 확장성을 고려한 설계를 통해 지속적인 발전이 가능한 시스템을 구축. <br>
  ( 참고: [지속 성장 가능한 소프트웨어를 만드는 방법](https://www.youtube.com/watch?v=pimYIfXCUe8) )
- 코프링(Kopring) 학습: 기존 자프링(Javapring) 이 아닌 새로운 학습

## 진행 기간
2024년 12월 14일 ~ (진행 중)

- [API 문서 링크](https://youngsu5582.github.io/lotto/)
- [서비스 링크](https://lotto.web.youngsu5582.life/)

## 인프라 구조도

![img.png](static/prod-infra-free-tier.png)

> 해당 인프라는 의도적으로 프리 티어만 사용하여 구성했습니다.
> ( micro, 복제 및 레플리카 X )

## 구동방법

### 개발

```bash
cd docker
docker-compose up -d
```
`docker ps --format '{{.Names}}' | grep 'lotto-back-' | xargs docker restart` : 여러개의 애플리케이션 동시 재시작


### 로컬

```bash
단순 LottoApplication 구동
```
