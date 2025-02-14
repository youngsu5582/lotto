#!/usr/bin/env python3

from itertools import combinations

def generate_lotto_insert_statements(output_file: str, batch_size: int = 1000):
    """
    로또 번호의 모든 가능한 조합(6개 선택)을 SQL INSERT 문으로 파일에 저장합니다.
    :param output_file: SQL 파일 경로
    :param batch_size: 한 번의 INSERT 문에 포함할 행 수
    """
    numbers = range(1, 46)  # 로또 번호 1부터 45
    combinations_count = 1  # ID 값 (1부터 시작)

    with open(output_file, 'w') as file:
        # 테이블 생성 스크립트 작성
        file.write("CREATE TABLE IF NOT EXISTS lotto (\n")
        file.write("  id BIGINT NOT NULL,\n")
        file.write("  numbers VARCHAR(255) NOT NULL,\n")
        file.write("  PRIMARY KEY (id)\n")
        file.write(");\n\n")

        # 인덱스 생성 스크립트 작성
        file.write("CREATE FULLTEXT INDEX idx_numbers ON lotto(numbers);\n\n")

        # 6개 숫자의 모든 조합 생성
        values = []
        for combo in combinations(numbers, 6):
            formatted_combination = ",".join(map(str, combo))
            values.append(f"({combinations_count}, '{formatted_combination}')")
            combinations_count += 1

            # 배치 크기에 도달하면 INSERT 문 작성
            if len(values) == batch_size:
                insert_statement = "INSERT INTO lotto (id, numbers) VALUES\n" + ",\n".join(values) + ";\n\n"
                file.write(insert_statement)
                values = []

        # 남아있는 값들 처리
        if values:
            insert_statement = "INSERT INTO lotto (id, numbers) VALUES\n" + ",\n".join(values) + ";\n\n"
            file.write(insert_statement)

    print(f"{combinations_count - 1}개의 조합이 생성되어 {output_file}에 저장되었습니다.")

# 실행
output_file = "lotto_combinations_batched.sql"
generate_lotto_insert_statements(output_file)
