import { BrowserRouter, NavLink, Route, Routes } from "react-router-dom";
import { useEffect, useState } from "react";
import "./styles.css";
import { createShort, myLinks, deleteByCode } from "./api";
import { keycloak } from "./keycloak";

function Header() {
  const [kcReady, setKcReady] = useState(false);
  useEffect(() => {
    setKcReady(true);
  }, []);
  // const user = keycloak.tokenParsed as any | undefined
  return (
    <div className="header">
      <div className="logo">URL Shortener</div>
      <div className="nav">
        <NavLink to="/" end>
          Главная
        </NavLink>
        <NavLink to="/links">Мои ссылки</NavLink>
        <NavLink to="/shorten">Создать</NavLink>
      </div>
      {/*<div className="row" style={{gap:8}}>*/}
      {/*  {keycloak.authenticated ? (*/}
      {/*    <>*/}
      {/*      <div className="small">{user?.preferred_username || user?.email}</div>*/}
      {/*      <button className="btn btn-danger" onClick={()=> keycloak.logout({ redirectUri: window.location.origin })}>Выйти</button>*/}
      {/*    </>*/}
      {/*  ) : (*/}
      {/*    <button className="btn" onClick={()=> keycloak.login({ redirectUri: window.location.origin })}>Войти</button>*/}
      {/*  )}*/}
      {/*</div>*/}
    </div>
  );
}

function Home() {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    if (keycloak.authenticated && keycloak.tokenParsed) {
      setUser(keycloak.tokenParsed);
    }
  }, []);

  if (!user) {
    return (
      <div className="card">
        <h2>Добро пожаловать</h2>
        <p className="small">Загрузка информации о пользователе...</p>
      </div>
    );
  }

  return (
    <div className="card">
      <h2>Добро пожаловать, {user.name || user.preferred_username}!</h2>
      
      <div style={{ marginTop: 20 }}>
        <h3>Информация о пользователе</h3>
        <div style={{ marginTop: 12, display: "flex", flexDirection: "column", gap: 8 }}>
          {user.given_name && user.family_name && (
            <div>
              <b>Имя:</b> {user.given_name} {user.middle_name ? `${user.middle_name} ` : ''}{user.family_name}
            </div>
          )}
          {user.email && (
            <div>
              <b>Email:</b> {user.email}
            </div>
          )}
          {user.phone_number && (
            <div>
              <b>Телефон:</b> {user.phone_number}
            </div>
          )}
          {user.address && (
            <div>
              <b>Адрес:</b> {user.address.locality}{user.address.region ? `, ${user.address.region}` : ''}{user.address.country ? `, ${user.address.country}` : ''}
            </div>
          )}
          {user.gender && (
            <div>
              <b>Пол:</b> {user.gender === 'male' ? 'Мужской' : user.gender === 'female' ? 'Женский' : user.gender}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function Shorten() {
  const [url, setUrl] = useState("");
  const [res, setRes] = useState<{
    shortUrl: string;
    originalUrl: string;
  } | null>(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);
  const submit = async () => {
    try {
      setErr(null);
      setLoading(true);
      const { data } = await createShort(url);
      setRes(data);
    } catch (e: any) {
      setErr(e?.response?.data?.message || "Ошибка");
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="card">
      <h2>Создать короткую ссылку</h2>
      <div className="row" style={{ gap: 8 }}>
        <input
          className="input"
          placeholder="https://example.com"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
        />
        <button className="btn" disabled={!url || loading} onClick={submit}>
          Создать
        </button>
      </div>
      {res && (
        <div style={{ marginTop: 10 }}>
          <div>
            Короткая: <b>{res.shortUrl}</b>
          </div>
          <div className="small">Оригинальная: {res.originalUrl}</div>
        </div>
      )}
      {err && <div style={{ color: "salmon", marginTop: 8 }}>{err}</div>}
    </div>
  );
}

function Links() {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);
  const load = async () => {
    try {
      setErr(null);
      setLoading(true);
      const { data } = await myLinks();
      setData(data);
    } catch (e: any) {
      setErr(e?.response?.data?.message || "Ошибка");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    load();
  }, []);
  const remove = async (code: string) => {
    try {
      await deleteByCode(code);
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || "Ошибка удаления");
    }
  };
  return (
    <div className="card">
      <div className="row" style={{ justifyContent: "space-between" }}>
        <h2>Мои ссылки</h2>
        <button className="btn" onClick={load} disabled={loading}>
          Обновить
        </button>
      </div>
      {data && (
        <>
          <div style={{ marginBottom: 10 }}>
            <b>Пользователь:</b> {data.user.fullName || data.user.username} (
            {data.user.email})
          </div>
          <table className="table">
            <thead>
              <tr>
                <th>Код</th>
                <th>Короткая</th>
                <th>Оригинальная</th>
                <th>Клики</th>
                <th>Создано</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data.links.map((l: any) => (
                <tr key={l.id}>
                  <td>{l.code}</td>
                  <td>{l.shortUrl}</td>
                  <td
                    style={{
                      maxWidth: 420,
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                      whiteSpace: "nowrap",
                    }}
                  >
                    {l.originalUrl}
                  </td>
                  <td>{l.clickCount}</td>
                  <td className="small">{l.createdAt}</td>
                  <td>
                    <button
                      className="btn btn-danger"
                      onClick={() => remove(l.code)}
                    >
                      Удалить
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {data.links.length === 0 && (
            <div className="small">Ссылок пока нет</div>
          )}
        </>
      )}
      {err && <div style={{ color: "salmon" }}>{err}</div>}
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <div className="container">
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/shorten" element={<Shorten />} />
          <Route path="/links" element={<Links />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
