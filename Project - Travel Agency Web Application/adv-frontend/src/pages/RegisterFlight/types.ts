

export type RegisterFlight = {
    departure:string
    destination:string
    airline:string
    departure_datetime:string
    destination_datetime:string
    airplane:string
    thirdClassPrice:number
    thirdClassSeats:number
    secondClassPrice:number
    secondClassSeats:number
    firstClassPrice:number
    firstClassSeats:number
}

export type RegisterFlightForm = {
    dd_datetime:any
    airline:string
    airplane:string
    departure:string
    destination:string
    thirdClassPrice:number
    thirdClassSeats:number
    secondClassPrice:number
    secondClassSeats:number
    firstClassPrice:number
    firstClassSeats:number
}

export interface Option {
    value: string;
    label: string;
}

