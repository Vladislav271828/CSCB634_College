import { useEffect, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';

const Form = ({ title, requestURL, requestEditId, successMsg, buttonMsg, formStructure, fetchUrl, editValues }) => {

    const [formData, setFormData] = useState({});
    const [fetchedEditValues, setFetchedEditValues] = useState(editValues ? editValues : {});
    const [fetchedSelections, setFetchedSelections] = useState({});
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setErrMsg("");
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(true)
        const url = requestURL.replace("{id}", requestEditId);
        try {
            var res
            fetchUrl || editValues ?
                res = await axiosPrivate.put(url, formData)
                : res = await axiosPrivate.post(url, formData)
            console.log(res.data);
            setErrMsg(successMsg);
        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    const fetchEditValues = async () => {
        try {
            const res = await axiosPrivate.get(fetchUrl);
            console.log(res.data);
            setFetchedEditValues(res.data);
        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    useEffect(() => {
        formStructure.map((input) => {
            if (input.type == "select" && input.fetchUrl && (formData[input.require] || !input.require)) {
                fetchOptions(input)
            }
        });
        if (fetchUrl) fetchEditValues();
    }, [])

    const fetchOptions = async (input) => {
        setSuccess(true)
        const url = input.fetchUrl.replace("{id}", formData[input.require]);
        try {
            const res = await axiosPrivate.get(url);
            console.log(res.data);
            setFetchedSelections({ ...fetchedSelections, [input.id]: res.data })
        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
        return null
    }

    return (
        <div className='component-container'>
            {title ? <h1>{title}</h1> : <></>}
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
            <form className='form-structure-container' onSubmit={handleSubmit}>
                {formStructure.map((input) => {
                    if (formData[input.require] || !input.require) {
                        if (input.type == "select") {
                            return <div key={input.id}>
                                <label>{input.label}</label>
                                <select
                                    name={input.id}
                                    onChange={handleChange}
                                >
                                    <option value={""} selected disabled hidden></option>
                                    {input.fetchUrl ?
                                        fetchedSelections[input.id]?.map((selection) => {
                                            return <option key={selection.id}
                                                value={selection.id}
                                                selected={selection.id == fetchedEditValues[input.id]}>{selection[input.fetchLabel]}</option>
                                        })
                                        : Object.entries(input.options).map(([value, text]) => {
                                            return <option key={value}
                                                value={value}
                                                selected={value == fetchedEditValues[input.id]}>{text}</option>
                                        })}
                                </select>
                            </div>
                        }
                        else {
                            return <div key={input.id}>
                                <label>{input.label}</label>
                                <input type={input.type}
                                    name={input.id}
                                    defaultValue={fetchedEditValues[input.id]}
                                    onChange={handleChange}
                                />
                            </div>
                        }
                    }
                })}
                <div className="btn">
                    <button type='submit'>
                        {buttonMsg}
                    </button>
                    <button type='button' onClick={() => navigate("../dashboard", { relative: "path" })}>
                        Go Back
                    </button>
                </div>
            </form>
        </div>

    )
}

export default Form