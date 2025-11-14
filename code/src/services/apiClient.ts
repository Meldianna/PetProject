import axios from "axios";

const baseURL =
  import.meta.env.VITE_API_BASE_URL ?? "/api/algoritmos";

export const apiClient = axios.create({
  baseURL,
  timeout: 15000,
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const message =
        error.response.data?.message ??
        error.response.data?.error ??
        "Error inesperado al comunicarse con la API.";
      return Promise.reject({
        message,
        status: error.response.status,
        data: error.response.data,
      });
    }
    if (error.request) {
      return Promise.reject({
        message:
          "No fue posible comunicarse con el servidor. Verifica que el backend está disponible.",
      });
    }
    return Promise.reject({ message: error.message ?? "Error desconocido." });
  },
);

