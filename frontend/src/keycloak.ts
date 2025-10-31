import Keycloak, { type KeycloakOnLoad } from "keycloak-js";

import { CONFIG } from "../config";
import { setToken } from "./api";

const redirectToPlatform = () => (window.location.href = CONFIG.platformUrl);

type InitialOptionsKeycloak = {
  url: string;
  realm: string;
  clientId: string;
  onLoad: KeycloakOnLoad;
};

const initOptions: InitialOptionsKeycloak = {
  url: CONFIG.authUrl,
  realm: "Alabuga",
  clientId: CONFIG.platformId,
  onLoad: "login-required",
};

export const keycloak = new Keycloak(initOptions);

export const initKeycloak = () => {
  const logout = () => {
    setToken(null);
    keycloak.logout();
  };

  let initPromise: Promise<void> | null = null;

  const connect = (): Promise<void> => {
    if (!initPromise) {
      initPromise = keycloak
        .init({
          onLoad: initOptions.onLoad,
          checkLoginIframe: false,
        })
        .then((authenticated) => {
          if (!authenticated) redirectToPlatform();

          // Set axios auth header
          setToken(keycloak.token ?? null);

          localStorage.setItem("user_token", keycloak.token ?? "");
          localStorage.setItem("refresh_token", keycloak.refreshToken ?? "");
          localStorage.setItem("user_uid", keycloak.tokenParsed?.sub ?? "");
          setInterval(async () => {
            try {
              const refreshed = await keycloak.updateToken(70);
              if (refreshed && localStorage.getItem("user_token")) {
                // Update axios auth header on refresh
                setToken(keycloak.token ?? null);
                localStorage.setItem("user_token", keycloak.token ?? "");
                localStorage.setItem(
                  "refresh_token",
                  keycloak.refreshToken ?? ""
                );
              }
              localStorage.setItem("user_uid", keycloak.tokenParsed?.sub ?? "");
            } catch (err) {
              console.error("Failed to refresh token", err);
              setToken(null);
            }
          }, 10000);
        })
        .catch((err) => {
          redirectToPlatform();
          console.error("Keycloak init failed", err);
        });
    }

    return initPromise;
  };

  return { logout, connect };
};
export const keycloakInstance = initKeycloak();
