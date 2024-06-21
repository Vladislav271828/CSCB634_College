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
    isPut,
    isDelete
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
        else {
            setLoading(true)
            setLevel(level - 1)
        }
    }

    function generateYearsArray(startYear) {
        const years = [];
        for (let year = startYear; year >= 2023; year--) {
            years.push({
                id: year + 'S',
                year: `${year}/${year + 1} Spring`
            });
            years.push({
                id: year + 'A',
                year: `${year}/${year + 1} Autumn`
            });
        }
        return years;
    }

    const handleForward = (id) => {
        setFormData({ ...formData, [formStructure[level].id]: id });
        setLoading(true)
        setLevel(level + 1)
    }

    const handleSkip = (skiplvl = 1) => {
        setLoading(true)
        setLevel(level + skiplvl)
    }

    useEffect(() => {
        if (!formStructure[level]?.type)
            fetchOptions();
    }, [level])

    const fetchOptions = async () => {
        setSuccess(true)
        if (formStructure[level].fetchUrl == "year") {
            const yeararr = generateYearsArray(new Date().getFullYear())
            setFetchedSelections(yeararr);
            setLoading(false)
        }
        else {
            let semester = "";
            const replace = Object.entries(formData).filter(([key, value]) => {
                return formStructure[level]?.require ? formStructure[level].require.includes(key) : false
            }).map(([key, value]) => {
                if (key == "year") {
                    semester = value.slice(4);
                    return value.slice(0, 4);
                }
                return value
            })
            // console.log(replace)
            const url = formStructure[level].require ?
                formatString(formStructure[level].fetchUrl, replace)
                : formStructure[level].fetchUrl;

            try {
                const res = await axiosPrivate.get(url);
                console.log(semester)
                let dataArray;
                if (semester) {
                    const filteredRes = res.data.filter((data) => {
                        return semester == 'A' ? data.autumn : !data.autumn
                    })
                    console.log(filteredRes)
                    dataArray = filteredRes;
                }
                else dataArray = res.data;

                if (formStructure[level]?.followUpUrl) {
                    const urlTwo = formStructure[level].require ?
                        formatString(formStructure[level].followUpUrl, replace)
                        : formStructure[level].followUpUrl;

                    const resTwo = await axiosPrivate.get(urlTwo);

                    let placeholderData = [];
                    resTwo.data.forEach((data) => {
                        placeholderData[data.id] = data[formStructure[level].replacement];
                    })

                    console.log(placeholderData)

                    dataArray.forEach((data) => {
                        const id = data[formStructure[level].fetchLabel];
                        data[formStructure[level].fetchLabel] = placeholderData[id]
                    })
                }

                setFetchedSelections(dataArray)

                setLoading(false)
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
    }
    if (!formStructure[level]?.type)
        return (
            <div className='component-container'>
                {title ? <h1>{title}</h1> : <></>}
                {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
                {formStructure[level]?.selectMsg ? <h3>{formStructure[level].selectMsg}</h3> : <></>}
                <SearchBar
                    search={search}
                    setSearch={setSearch}
                />
                <div className='select-list'>
                    {formStructure[level]?.skip && <div className="btn skip-btn">
                        <button type='button' onClick={() => handleSkip(formStructure[level]?.skiplvl)}>
                            {formStructure[level]?.skip}
                        </button>
                    </div>}
                    {loading && <p>Loading...</p>}

                    {!loading && fetchedSelections.length ? (
                        fetchedSelections.filter((name) => (
                            (name[formStructure[level].fetchLabel] ? name[formStructure[level].fetchLabel] : "").toLowerCase()).includes(search.toLowerCase()
                            )).sort().map((item) => {
                                return <section key={item.id}
                                    className="select-list-selection"
                                    onClick={() => handleForward(formStructure[level]?.altId ? item[formStructure[level].altId] : item.id)}>
                                    <p style={{ fontWeight: "600" }}>{item[formStructure[level].fetchLabel] + (formStructure[level].fetchLabelAdd ? " " + item[formStructure[level].fetchLabelAdd] : "")}</p>
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
                isDelete={isDelete}
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