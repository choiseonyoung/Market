# ♻️멋사 마켓
> 🥕 당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.
> 사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 거래 플랫폼 백엔드를 구현했습니다.

<br />

## 📆 개발 기간
2022.06.29 ~ 2023.07.05
<br />
2023.07.26 ~ 2023.08.02

<br />

## 🔨 개발 환경
- IDE : IntelliJ
- Language : Java 17
- Framework : SpringBoot 3.1.1
- Database : SQLite
- Dependencies
  - Spring Web
  - Spring Data JPA
  - Spring Boot DevTools
  - Lombok
  - Validation   

<br />

## 📋 ERD


<img src="https://github.com/likelion-backend-5th/Project_1_SeoHyunKim/assets/44860284/a9563c16-badf-4d8b-a14d-9acf7c947bd4">


<br />

## 📌 구현 기능
### 1️⃣ 중고 물품 관리
<details>
<summary>요구사항</summary>
<div markdown="1">

    1. 중고 거래를 목적으로 물품에 대한 정보를 등록할 수 있다.
        1. 이때 반드시 포함되어야 하는 내용은 제목, 설명, 최소 가격, 작성자 이다.
        2. 또한 사용자가 물품을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
        3. 최초로 물품이 등록될 때, 중고 물품의 상태는 판매중 상태가 된다.
    2. 등록된 물품 정보는 누구든지 열람할 수 있다. 
        1. 페이지 단위 조회가 가능하다.
        2. 전체 조회, 단일 조회 모두 가능하다.
    3. 등록된 물품 정보는 수정이 가능하다. 
        1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
    4. 등록된 물품 정보에 이미지를 첨부할 수 있다.
        1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
        2. 이미지를 관리하는 방법은 자율이다.
    5. 등록된 물품 정보는 삭제가 가능하다. 
        1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.

</div>
</details>

<details>
<summary>Endpoints</summary>
<div markdown="1">

- `POST /items`

  Request Body:

    ```json
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1000000,
        "writer": "csy",
        "password": "1qaz2wsx"
    }
    ```

  Response Status: 200

  Response Body:

    ```json
    {
        "message": "등록이 완료되었습니다."
    }
    ```

- `GET /items?page={page}&limit={limit}`

  Request Body: 없음

  Response Status: 200

  Response Body:

    ```json
    {
        "content": [
          {
                "title": "콜드브루 드립기 팝니다",
                "description": "ㅈㄱㄴ",
                "minPriceWanted": 20000,
                "imageUrl": "images\\0f7f8941-72fb-40e3-84a4-2c492d53accb.jpg",
                "status": "판매완료"
            },
              {
                "title": "중고 맥북 팝니다",
                "description": "2019년 맥북 프로 13인치 모델입니다",
                "minPriceWanted": 1000000,
                "imageUrl": null,
                "status": "판매중"
            }, 
            // ...
        ],
        "totalPages": 4,
        "totalElements": 100,
        "last": false,
        "size": 25,
        "number": 1,
        "numberOfElements": 25,
        "first": false,
        "empty": false
    }
    ```

- `GET /items/{itemId}`

  Request Body: 없음

  Response Status: 200

  Response Body:

    ```json
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1000000,
        "status": "판매중"
    }
    ```

- `PUT /items/{itemId}`

  Request Body:

    ```json
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1250000,
        "writer": "csy",
        "password": "1qaz2wsx"
    }
    ```

  Response Body:

    ```json
    {
        "message": "물품이 수정되었습니다."
    }
    ```

- `PUT /items/{itemId}/image`

  Request Body (Form Data):

    ```
    image:    image.jpg (file)
    writer:   csy
    password: 1qaz2wsx
    ```

  Response Body:

    ```json
    {
        "message": "이미지가 등록되었습니다."
    }
    ```

- `DELETE /items/{itemId}`

  Request Body:

    ```json
    {
        "writer": "csy",
        "password": "1qaz2wsx"
    }
    ```

  Response Body:

    ```json
    {
        "message": "물품을 삭제했습니다."
    }
    ```

</div>
</details>


### 2️⃣ 중고 물품 댓글
<details>
<summary>요구사항</summary>
<div markdown="1">

    1. 등록된 물품에 대한 질문을 위하여 댓글을 등록할 수 있다. 
      1. 이때 반드시 포함되어야 하는 내용은 대상 물품, 댓글 내용, 작성자이다.
      2. 또한 댓글을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
    2. 등록된 댓글은 누구든지 열람할 수 있다. 
      1. 페이지 단위 조회가 가능하다.
    3. 등록된 댓글은 수정이 가능하다. 
      1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
    4. 등록된 댓글은 삭제가 가능하다. 
      1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
    5. 댓글에는 초기에 비워져 있는 답글 항목이 존재한다. 
      1. 만약 댓글이 등록된 대상 물품을 등록한 사람일 경우, 물품을 등록할 때 사용한 비밀번호를 첨부할 경우 답글 항목을 수정할 수 있다.
      2. 답글은 댓글에 포함된 공개 정보이다.
   
</div>
</details>
<details>
<summary>EndPoints</summary>
<div markdown="1">

- `POST /items/{itemId}/comments`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234",
        "content": "할인 가능하신가요?"
    }
    ```

  Response Status: 200

  Response Body:

    ```json
    {
        "message": "댓글이 등록되었습니다."
    }
    ```

- `GET /items/{itemId}/comments`

  Request Body: 없음

  Response Status: 200

  Response Body:

    ```json
    {
        "content": [
    	      {
                "id": 1,
    			"content": "할인 가능하신가요?",
                "reply": "아니요"
            },
            // ...
        ],
        "totalPages": 4,
        "totalElements": 100,
        "last": false,
        "size": 25,
        "number": 1,
        "numberOfElements": 25,
        "first": false,
        "empty": false
    }
    ```

- `PUT /items/{itemId}/comments/{commentId}`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234",
        "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다"
    }
    ```

  Response Body:

    ```json
    {
        "message": "댓글이 수정되었습니다."
    }
    ```

- `PUT /items/{itemId}/comments/{commentId}/reply`

  Request Body:

    ```json
    {
        "writer": "csy",
        "password": "1qaz2wsx",
        "reply": "안됩니다"
    }
    ```

  Response Body:

    ```json
    {
        "message": "댓글에 답변이 추가되었습니다."
    }
    ```

- `DELETE /items/{itemId}/comments/{commentId}`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234"
    }
    ```

  Response Body:

</div>

</details>

### 3️⃣ 구매 제안
<details>
<summary>요구사항</summary>
<div markdown="1">

    1. 등록된 물품에 대하여 구매 제안을 등록할 수 있다. 
      1. 이때 반드시 포함되어야 하는 내용은 대상 물품, 제안 가격, 작성자이다.
      2. 또한 구매 제안을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
      3. 구매 제안이 등록될 때, 제안의 상태는 제안 상태가 된다.
    2. 구매 제안은 대상 물품의 주인과 등록한 사용자만 조회할 수 있다.
      1. 대상 물품의 주인은, 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다. 이때 물품에 등록된 모든 구매 제안이 확인 가능하다. 페이지 기능을 지원한다.
      2. 등록한 사용자는, 조회를 위해서 자신이 사용한 작성자와 비밀번호를 첨부해야 한다. 이때 자신이 등록한 구매 제안만 확인이 가능하다. 페이지 기능을 지원한다.
    3. 등록된 제안은 수정이 가능하다.
      1. 이때, 제안이 등록될때 추가한 작성자와 비밀번호를 첨부해야 한다.
    4. 등록된 제안은 삭제가 가능하다.
      1. 이때, 제안이 등록될때 추가한 작성자와 비밀번호를 첨부해야 한다.
    5. 대상 물품의 주인은 구매 제안을 수락할 수 있다.
      1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
      2. 이때 구매 제안의 상태는 수락이 된다.
    6. 대상 물품의 주인은 구매 제안을 거절할 수 있다.
      1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
      2. 이때 구매 제안의 상태는 거절이 된다.
    7. 구매 제안을 등록한 사용자는, 자신이 등록한 제안이 수락 상태일 경우, 구매 확정을 할 수 있다.
      1. 이를 위해서 제안을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
      2. 이때 구매 제안의 상태는 확정 상태가 된다.
      3. 구매 제안이 확정될 경우, 대상 물품의 상태는 판매 완료가 된다.
      4. 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 거절이 된다.

</div>
</details>

<details>
<summary>Endpoints</summary>
<div markdown="1">

- `POST /items/{itemId}/proposals`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234",
        "suggestedPrice": 1000000
    }
    ```

  Response Status: 200

  Response Body:

    ```json
    {
        "message": "구매 제안이 등록되었습니다."
    }
    ```

- `GET /items/{itemId}/proposals?writer=jeeho.edu&password=qwerty1234&page=1`

  Request Body: 없음

  Response Status: 200

  Response Body:

    ```json
    {
        "content": [
    	      {
                "id": 1,
    			"suggestedPrice": 1000000,
                "status": "거절"
            },
    	      {
                "id": 2,
    			"suggestedPrice": 1200000,
                "status": "제안"
            },
            // ...
        ],
        "totalPages": 4,
        "totalElements": 100,
        "last": false,
        "size": 25,
        "number": 1,
        "numberOfElements": 25,
        "first": false,
        "empty": false
    }
    ```

- `PUT /items/{itemId}/proposals/{proposalId}`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234",
        "suggestedPrice": 1100000
    }
    ```

  Response Body:

    ```json
    {
        "message": "제안이 수정되었습니다."
    }
    ```

  `writer` 와 `password` 가 물품 등록할 때의 값과 일치하지 않을 경우 실패

- `DELETE /items/{itemId}/proposals/{proposalId}`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234"
    }
    ```

  Response Body:

    ```json
    {
        "message": "제안을 삭제했습니다."
    }
    ```

  `writer` 와 `password` 가 제안 등록할 때의 값과 일치하지 않을 경우 실패

- `PUT /items/{itemId}/proposals/{proposalId}`

  Request Body:

    ```json
    {
        "writer": "csy",
        "password": "1qaz2wsx",
        "status": "수락" || "거절"
    }
    ```

  Response Body:

    ```json
    {
        "message": "제안의 상태가 변경되었습니다."
    }
    ```

  `writer` 와 `password` 가 물품 등록할 때의 값과 일치하지 않을 경우 실패

- `PUT /items/{itemId}/proposals/{proposalId}`

  Request Body:

    ```json
    {
        "writer": "sysy",
        "password": "qwerty1234",
        "status": "확정"
    }
    ```

  Response Body:

    ```json
    {
        "message": "구매가 확정되었습니다."
    }
    ```

  `writer` 와 `password` 가 제안 등록할 때의 값과 일치하지 않을 경우 실패

  제안의 상태가 수락이 아닐 경우 실패

</div>
</details>

### 4️⃣ 인증 만들기
<details>
<summary>요구사항</summary>
<div markdown="1">

    💡 본래 만들었던 서비스에 사용자 인증을 첨부합니다.

    1. 사용자는 **회원가입**을 진행할 수 있다.
    - 회원가입에 필요한 정보는 아이디와 비밀번호가 필수이다.
    - 부수적으로 전화번호, 이메일, 주소 정보를 기입할 수 있다.
    - 이에 필요한 사용자 Entity는 직접 작성하도록 한다.

    2. **아이디 비밀번호**를 통해 로그인을 할 수 있어야 한다.

    3. 아이디 비밀번호를 통해 로그인에 성공하면, **JWT가 발급**된다. 이 JWT를 소유하고 있을 경우 **인증**이 필요한 서비스에 접근이 가능해 진다.
    - 인증이 필요한 서비스는 추후(미션 후반부) 정의한다.

    4. JWT를 받은 서비스는 **사용자가 누구인지** 사용자 **Entity를 기준**으로 정확하게 판단할 수 있어야 한다.

</div>
</details>

### 5️⃣ 관계 설정
<details>
<summary>요구사항</summary>
<div markdown="1">

    💡 이전에 만든 물품, 댓글들에 대한 데이터베이스 테이블을, 사용자 정보를 포함하여 고도화 합니다.
    
    1. 아이디와 비밀번호를 필요로 했던 테이블들은 실제 사용자 Record에 대응되도록 ERD를 수정하자.
    - ERD 수정과 함께 해당 정보를 적당히 표현할 수 있도록 Entity를 재작성하자.
    - 그리고 ORM의 기능을 충실히 사용할 수 있도록 어노테이션을 활용한다.

    2. 다른 작성한 Entity도 변경을 진행한다.
    - 서로 참조하고 있는 테이블 관계가 있다면, 해당 사항이 표현될 수 있도록 Entity를 재작성한다.

</div>
</details>

### 6️⃣ 기능 접근 설정
<details>
<summary>요구사항</summary>
<div markdown="1">

    💡 기능들의 사용 가능 여부가 사용자의 인증 상태에 따라 변동하도록 제작합니다.

    1. 본래 “누구든지 열람할 수 있다”의 기능 목록은 사용자가 **인증하지 않은 상태**에서 사용할 수 있도록 한다.
    - 등록된 물품 정보는 누구든지 열람할 수 있다.
    - 등록된 댓글은 누구든지 열람할 수 있다.
    - 기타 기능들

    2. 작성자와 비밀번호를 포함하는 데이터는 **인증된 사용자만 사용**할 수 있도록 한다.
    - 이때 해당하는 기능에 포함되는 아이디 비밀번호 정보는, 1일차에 새로 작성한 사용자 Entity와의 관계로 대체한다.
        - 물품 정보 등록 → 물품 정보와 사용자 관계 설정
        - 댓글 등록 → 댓글과 사용자 관계 설정
        - 기타 등등
    - 누구든지 중고 거래를 목적으로 물품에 대한 정보를 등록할 수 있다.
    - 등록된 물품에 대한 질문을 위하여 댓글을 등록할 수 있다.
    - 등록된 물품에 대하여 구매 제안을 등록할 수 있다.
    - 기타 기능들

</div>
</details>
