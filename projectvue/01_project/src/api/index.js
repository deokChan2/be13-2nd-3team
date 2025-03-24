import axios from "axios";

// Axios
//  - HTTP 기반 통신을 지원하는 자바스크립트 라이브러리이다.
//  - 브라우저 호환성, JSON 자동 변환 기능 등의 장점을 가지고 있다.

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 2000,
});



export default apiClient;