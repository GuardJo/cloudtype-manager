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

    if (httpStatus >= 300) {
        if (httpStatus === 401) {
            window.location.href = '/login'
        } else if (httpStatus === 404) {
            window.location.href = '/not-found'
        } else {
            window.location.href = '/error'
        }

        throw new Error(`failed API request, statusCode = ${httpStatus}`)
    }

    return response.json()
}