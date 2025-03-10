import json
import requests
import pymysql
from bs4 import BeautifulSoup
from datetime import datetime

LOTTO_URL = "https://www.dhlottery.co.kr/common.do?method=main&mainMode=default"

def get_latest_draw():
    """
    동행복권 사이트에서 최신 로또 당첨번호 크롤링
    Returns: (round, draw_date, [6개 번호], bonus 번호) or None
    """
    try:
        resp = requests.get(LOTTO_URL, timeout=10)
        resp.raise_for_status()
        soup = BeautifulSoup(resp.text, 'html.parser')
        content_div = soup.find('div', {'class': 'content'})
        if not content_div:
            return None

        round_str = content_div.find('strong', {'id': 'lottoDrwNo'}).text.strip()
        round_no = int(round_str)

        # 'drwNoDate' 예: "(2023-02-25 추첨)"
        date_str = content_div.find('span', {'id': 'drwNoDate'}).text.strip()
        # 여기서는 단순히 "2023-02-25" 만 추출한다고 가정:
        date_clean = date_str.strip('()추첨').strip()

        nums = []
        for i in range(1, 7):
            val = content_div.find('span', {'id': f'drwtNo{i}'}).text.strip()
            nums.append(int(val))
        bonus = int(content_div.find('span', {'id': 'bnusNo'}).text.strip())

        return {
            'round': round_no,
            'draw_date': date_clean,
            'numbers': nums,
            'bonus': bonus
        }
    except Exception as e:
        print("Error in get_latest_draw:", e)
        return None

def main():
    # 1) 크롤링
    draw_info = get_latest_draw()
    if not draw_info:
        print("크롤링 실패. 종료.")
        return {"status": "fail", "reason": "crawling error"}

    print(draw_info)

if __name__ == "__main__":
	main()
