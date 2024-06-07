import { useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';

const Form = ({ title, requestURL, successMsg, formStructure }) => {

    const [formData, setFormData] = useState({});
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
        try {
            const res = await axiosPrivate.post(requestURL, formData);
            console.log(res);
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

    return (
        <div className='component-container'>
            <h1>{title}</h1>
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
            <form className='form-structure-container' onSubmit={handleSubmit}>
                {formStructure.map((input) => {
                    if (input.type == "select") {
                        return <div key={input.id}>
                            <label>{input.label}</label>
                            <select
                                name={input.id}
                                // value={form?.colorRGB}
                                onChange={handleChange}
                            >
                                <option value={""}></option>
                                {Object.entries(input.options).map(([value, text]) => {
                                    return <option key={value} value={value}>{text}</option>
                                })}
                            </select>
                        </div>
                    }
                    else {
                        return <div key={input.id}>
                            <label>{input.label}</label>
                            <input type={input.type}
                                name={input.id}
                                // defaultValue={name}
                                onChange={handleChange}
                            />
                        </div>
                    }
                })}
                <div className="btn">
                    <button type='submit'>
                        Save Changes
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