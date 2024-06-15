import { useEffect, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import SearchBar from './SearchBar';
import Form from './Form';

const SelectList = ({ title,
    requestURL,
    requestIds,
    successMsg,
    buttonMsg,
    formStructure,
    fetchUrl,
    deleteWarningMsg,
    deleteUrl,
    isPut
}) => {

    const [formData, setFormData] = useState({});
    const [fetchedSelections, setFetchedSelections] = useState([]);
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);
    const [level, setLevel] = useState(0)
    const [search, setSearch] = useState('');
    const [loading, setLoading] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    function formatString(string, params) {
        return string.replace(/{(\d+)}/g, (match, index) => {
            return typeof params[index] !== 'undefined' ? params[index] : match;
        });
    }

    const handleBack = () => {
        if (level <= 0)
            navigate("../dashboard", { relative: "path" });
        else setLevel(level - 1)
    }

    const handleForward = (id) => {
        setFormData({ ...formData, [formStructure[level].id]: id });
        setLevel(level + 1)
    }

    useEffect(() => {
        if (!formStructure[level]?.type)
            fetchOptions();
    }, [level])

    const fetchOptions = async () => {
        setSuccess(true)
        setLoading(true)
        const replace = Object.entries(formData).filter(([key, value]) => {
            return formStructure[level]?.require ? formStructure[level].require.includes(key) : false
        }).map(([key, value]) => { return value })
        const url = formStructure[level].require ?
            formatString(formStructure[level].fetchUrl, replace)
            : formStructure[level].fetchUrl;
        try {
            const res = await axiosPrivate.get(url);
            setFetchedSelections(res.data)
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
        setLoading(false)
    }
    if (!formStructure[level]?.type)
        return (
            <div className='component-container'>
                {title ? <h1>{title}</h1> : <></>}
                {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
                <SearchBar
                    search={search}
                    setSearch={setSearch}
                />
                <div className='select-list'>
                    {loading && <p>Loading...</p>}

                    {fetchedSelections.length ? (
                        fetchedSelections.filter((name) => (
                            (name[formStructure[level].fetchLabel]).toLowerCase()).includes(search.toLowerCase()
                            )).sort().map((item) => {
                                return <section key={item.id}
                                    className="select-list-selection"
                                    onClick={() => handleForward(item.id)}>
                                    <p style={{ fontWeight: "600" }}>{item[formStructure[level].fetchLabel]}</p>
                                    <p>{item[formStructure[level].fetchLabelSecond]}</p>
                                </section>
                            })) : <p>There are no options available.</p>}

                    {!loading && <div className="btn">
                        <button type='button' onClick={() => handleBack()}>
                            Go Back
                        </button>
                    </div>}
                </div>
            </div>
        )
    else if (formStructure[level].type == "FORM") {
        const reqIds = Object.entries(formData).filter(([key, value]) => {
            return requestIds ? requestIds.includes(key) : false
        }).reduce((acc, [key, value]) => {
            acc.push(value)
            return acc
        }, [])
        return (
            <Form
                title={title}
                requestURL={requestURL}
                requestIds={reqIds}
                successMsg={successMsg}
                buttonMsg={buttonMsg}
                formStructure={formStructure[level].data}
                selectListValues={{ ...formData }}
                fetchUrl={fetchUrl}
                deleteWarningMsg={deleteWarningMsg}
                deleteUrl={deleteUrl}
                isPut={isPut}
                backFunc={handleBack} />
        )
    }
    else
        return (
            <>{!loading && <div className="btn">
                <button type='button' onClick={() => handleBack()}>
                    Go Back
                </button>
            </div>}</>
        )
}

export default SelectList