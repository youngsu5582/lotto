import re
from datetime import datetime

input_file = 'lotto_draw.sql'
output_file = 'lotto_draw_convert.sql'

# 1) 정규식: 'YYYY.M.D' (연도4자리, 월/일은 1~2자리)
#   작은따옴표(') 포함하여 매칭
pattern = re.compile(r"'(\d{4})\.(\d{1,2})\.(\d{1,2})'")

def replacer(match):
    year = int(match.group(1))
    month = int(match.group(2))
    day = int(match.group(3))
    # datetime으로 변환 후, 다시 YYYY-MM-DD 형태로 포맷
    dt = datetime(year, month, day)
    return f"'{dt.strftime('%Y-%m-%d')}'"

# 2) input.sql 파일 읽기
with open(input_file, 'r', encoding='utf-8') as f:
    content = f.read()

# 3) 정규식으로 치환
new_content = pattern.sub(replacer, content)

# 4) 결과를 output.sql에 저장
with open(output_file, 'w', encoding='utf-8') as f:
    f.write(new_content)
