import {type ClassValue, clsx} from "clsx"
import {twMerge} from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

export const getAuthHeaders = () => {
    const token = localStorage.getItem('authToken');
    return {
        "Content-Type": "application/json",
        ...(token && {"Authorization": `Bearer ${token}`}),
    };
};

export const validateResponse = (response: Response) => {
    const httpStatus = response.status;

    if (httpStatus === 401) {
        window.location.href = '/login'
    }
    
    return response.json()
}