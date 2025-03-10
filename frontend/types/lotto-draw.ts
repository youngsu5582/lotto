import { List } from "postcss/lib/list";

export interface LottoDrawPreviewNumberResponse {
    success: boolean;
    status: number;
    message: string;
    data: LottoDrawPrivewResponse
} 
export interface LottoNumberCountResponse{
    number: number;
    count: number;    
}
export interface LottoDrawPrivewResponse{
    lottoNumberCountResponse: LottoNumberCountResponse[],
    lottoResultResponse: LottoResultResponse[]
}
 
export interface LottoResultResponse{
    round: number;
    lottoDrawResponse : LottoDrawResponse,
    drawResultResponse : DrawResultResponse
}

export interface LottoDrawResponse{
    date: Date,
    numbers: number[],
    bonus: number
}
export interface DrawResultResponse{
    matchCount: number,
    bonusMatch: boolean
}