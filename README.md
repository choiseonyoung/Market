# ♻️멋사 마켓
> 🥕 당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.
> 사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 거래 플랫폼 백엔드를 구현했습니다.   

## 📆 개발 기간
2022.06.29 ~ 2023.07.05   

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

## 📋 ERD

<img width="770" alt="erd" src="https://github.com/likelion-backend-5th/MiniProject_Basic_ChoiSeonYoung/assets/44860284/05f94f30-ea34-4fae-a8f9-d7a4ee6d7132">   

## 📌 구현 기능
### 1️⃣ 중고 물품 관리
<details>
<summary>요구사항</summary>
<div markdown="1">

    1. 등록된 물품에 대하여 구매 제안을 등록할 수 있다. 
        1. 이때 반드시 포함되어야 하는 내용은 대상 물품, 제안 가격, 작성자이다.
        2. 또한 구매 제안을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
        3.구매 제안이 등록될 때, 제안의 상태는 제안 상태가 된다.
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
                "id": 1,
                "title": "중고 맥북 팝니다",
                "description": "2019년 맥북 프로 13인치 모델입니다",
                "minPriceWanted": 1000000,
                "status": "판매중"
            },
              {
                "id": 2,
                "title": "콜드브루 드립기 팝니다",
                "description": "ㅈㄱㄴ",
                "minPriceWanted": 20000,
                "imageUrl": "images\\0f7f8941-72fb-40e3-84a4-2c492d53accb.jpg",
                "status": "판매완료"
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