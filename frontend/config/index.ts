export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
export const TOSS_CLIENT_KEY = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY||'';
if (TOSS_CLIENT_KEY.length==0) {
  throw new Error('NEXT_PUBLIC_TOSS_CLIENT_KEY 환경 변수가 설정되지 않았습니다.');
}
